using System.Collections;
using System.Collections.Generic;
using System.IO;
using System;
using System.Runtime.Serialization.Formatters.Binary;
using UnityEngine;
using Unity.Jobs;
using Unity.Burst;
using Unity.Collections;

using static Unity.Mathematics.math;

public static class BoardLibrary {

    // Summary:
    //     Function type of if a pawn can be rendered at given board position.
    // Parameters:
    //     x:
    //         x value of board position.
    //     y:
    //         y value of board position.
    public delegate bool BoundsChecker(int x, int y);

    // Summary:
    //     Types of boards.
    public enum BoardType {
        English, 
        European, 
        Asymmetrical, 
        German, 
        Diamond,
    }

    // Summary:
    //     Board objects that the game is moved on.
    private readonly static Board[] boards = {
        new English(), 
        new European(), 
        new Asymmetrical(), 
        new German(), 
        new Diamond(),
    };

    // Summary:
    //     Executed at start.
    static BoardLibrary(){
        // NativeArray objects have to be disposed by hand
        // thus this function is added to the functions that
        // are executed when application is quitted
        OnQuitExecuter.AddFuncToExecuteOnQuit(BoardsDestructor);
    } 

    // Summary:
    //     Disposes allocations.
    private static void BoardsDestructor(){
        foreach(Board board in boards){
            board.pawnActivenessArray.Dispose();
            board.boardActivenessArray.Dispose();
        } 
    }
    
    // Summary:
    //     Returns corresponding board object for given board type.
    // Parameters:
    //     name:
    //         Name/type of the board.
    public static Board GetBoard(BoardType name){
        return boards[(int) name];
    }

    
    // Summary:
    //     Saves corresponding board object for given board type.
    // Parameters:
    //     name:
    //         Name/type of the board.
    public static void SaveBoard(BoardType name){
        BinaryFormatter formatter = new BinaryFormatter();
        string path = Application.persistentDataPath + "/" + name.ToString();
        FileStream stream = new FileStream(path, FileMode.Create);

        formatter.Serialize(stream, new BoardData(GetBoard(name)));
        stream.Close();
    }

    // Summary:
    //     Loads corresponding board object for given board type.
    // Parameters:
    //     name:
    //         Name/type of the board.
    public static void LoadBoard(BoardType name){
        string path = Application.persistentDataPath + "/" + name.ToString();

        if(File.Exists(path)){
            BinaryFormatter formatter = new BinaryFormatter();
            FileStream stream = new FileStream(path, FileMode.Open);

            boards[(int) name].LoadData((BoardData) formatter.Deserialize(stream));

            stream.Close();
        }
        else{
            throw new NoBoardSaveException();
        }
    }

    // Summary:
    //     Board class of the game Peg Solitaire.
    [System.Serializable]    
    public abstract class Board{

        // Summary:
        //     Action recorder of the board.
        private ActionRecorder _actionRecorder;
        public ActionRecorder actionRecorder => _actionRecorder;

        // Summary:
        //     Array of values representing if pawn is to be rendered or not.
        private NativeArray<uint> _pawnActivenessArray;
        public NativeArray<uint> pawnActivenessArray => _pawnActivenessArray;

        // Summary:
        //     Array of values representing if board unit is to be rendered or not.
        private NativeArray<uint> _boardActivenessArray;
        public NativeArray<uint> boardActivenessArray => _boardActivenessArray;

        // Summary:
        //     Board position indexes where a pawn can be moved to.
        public List<int> playableHoles = new List<int>();

        // Summary:
        //     Function that checks if a pawn can be rendered at given board position.
        public abstract BoundsChecker boundsChecker {get;}

        // Summary:
        //     Index of the empty position at the start of the game.
        private int _startIndex;

        // Summary:
        //     Width of the board.
        private int _width;
        public int width => _width;

        // Summary:
        //     Height of the board.
        private int _height;
        public int height => _height;

        // Summary:
        //     Sets pawn activeness array. Job scheduler compiled with burst for further optimization.
        [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
        struct HashJob : IJobFor {
            
            // Summary:
            //     Array to be set.
            [WriteOnly]
            public NativeArray<uint> activenessArray;

            // Summary:
            //     Function to get what to write.
            public FunctionPointer<BoundsChecker> activeChecker;

            // Summary:
            //     Size.
            public int width, height;
            
            // Summary:
            //     Executed with index scheduler gives.
            // Parameters:
            //     i:
            //         Execution index.
            public void Execute(int i){
                // Get board position of given index
                int y = (int) floor(i / width + 0.00001f);
                int x = i - y * width;

                y -= height / 2;
                x -= width / 2;

                // Set if it should be rendered or not
                activenessArray[i] = Convert.ToUInt32(activeChecker.Invoke(x, y));
            }
        }

        // Summary:
        //     Initializes with given width and height and sets start point as middle.
        // Parameters:
        //     width:
        //         Width of the board.
        //     height:
        //         Height of the board.
        public Board(int width, int height) : this(width, height, width / 2 , height / 2) { }

        // Summary:
        //     Initializes with given width and height and sets start point as given.
        // Parameters:
        //     width:
        //         Width of the board.
        //     height:
        //         Height of the board.
        //     startX:
        //         x value of the start point.
        //     startY:
        //         y value of the start point.
        public Board(int width, int height, int startX, int startY) : this(width, height, startX + startY * width) { }

        // Summary:
        //     Initializes with given width and height and sets start point index as given.
        // Parameters:
        //     width:
        //         Width of the board.
        //     height:
        //         Height of the board.
        //     startIndex:
        //         Index of the start point.
        public Board(int width, int height, int startIndex){
            _width = width;
            _height = height;
            _startIndex = startIndex;

            OnEnable();

            // Board activeness array is basically the same as pawn activeness array
            // but it is not updated for the contrary, since board won't be changed
            _boardActivenessArray = new NativeArray<uint>(_pawnActivenessArray, Allocator.Persistent);

            // Empty the position where start should be             
            playableHoles.Add(startIndex);
            _pawnActivenessArray[startIndex] = 0u;
        }

        // Summary:
        //     Initializes with given data.
        // Parameters:
        //     boardData:
        //         Data to initialize the board with.
        public Board(BoardData boardData){
            LoadData(boardData);
        }
        
        ~Board(){
            OnDisable();
            _boardActivenessArray.Dispose();
        }

        // Summary:
        //     Sets up the board, called upon initialization.
        void OnEnable() {
            _actionRecorder = new ActionRecorder();
            _pawnActivenessArray = new NativeArray<uint>(width * height, Allocator.Persistent);

            new HashJob{
                activenessArray = _pawnActivenessArray,
                activeChecker = BurstCompiler.CompileFunctionPointer<BoundsChecker>(boundsChecker),
                width = width,
                height = height,
            }.ScheduleParallel(width * height, width, default).Complete();
        }

        void OnDisable () {
            _pawnActivenessArray.Dispose();
        }

        void OnValidate () {
            OnDisable();
            OnEnable();
        }

        // Summary:
        //     Returns status of pawn at given board position.
        // Parameters:
        //     vec:
        //         Board position.
        public bool GetActive(Vector2Int vec){
            return GetActive(vec.x, vec.y);
        }

        // Summary:
        //     Returns status of pawn at given board position.
        // Parameters:
        //     x:
        //         x value of board position.
        //     y:
        //         y value of board position.
        public bool GetActive(int x, int y){
            return GetActive(x + y * width);
        }
        
        // Summary:
        //     Returns status of pawn at given index.
        // Parameters:
        //     i:
        //         Index of pawn.
        public bool GetActive(int i){
            if(IsIndexValid(i)){
                return _pawnActivenessArray.GetAsBool(i);
            }
            throw new OutOfBoardException();
        }

        // Summary:
        //     Sets status of pawn at given board position.
        // Parameters:
        //     pos:
        //         Board position of the pawn whose value to be set.
        //     value:
        //         New render value for the pawn.
        public void SetActive(Vector2Int pos, bool value){
            SetActive(pos.x, pos.y, value);
        }

        // Summary:
        //     Sets status of pawn at given board position.
        // Parameters:
        //     x:
        //         x value of board position.
        //     y:
        //         y value of board position.
        //     value:
        //         New render value for the pawn.
        public void SetActive(int x, int y, bool value){
            SetActive(x + y * width, value);
        }

        // Summary:
        //     Sets status of pawn at given board position.
        // Parameters:
        //     i:
        //         Index of pawn.
        //     value:
        //         New render value for the pawn.
        public void SetActive(int i, bool value){
            if(i >= _pawnActivenessArray.Length){
                throw new OutOfBoardException();
            }
            _pawnActivenessArray[i] = Convert.ToUInt32(value);
        }

        // Name of the function explains well enough.
        public int BoardPositionToIndex(Vector2Int vec){
            return BoardPositionToIndex(vec.x, vec.y);
        }

        // Name of the function explains well enough.
        public int BoardPositionToIndex(int x, int y){
            x += width / 2;
            y += height / 2;
            return x + y * width;
        }

        // Name of the function explains well enough.
        public Vector2Int IndexToBoardPosition(int index){
            int y = (int) floor(index / width + 0.00001f);
            int x = index - y * width;

            y -= height / 2;
            x -= width / 2;

            return new Vector2Int(x, y);
        }

        // Name of the function explains well enough.
        public void Reset(){
            OnValidate();
            _actionRecorder.Clear();
            playableHoles.Add(_startIndex);
            _pawnActivenessArray[_startIndex] = 0;
        }

        // Summary:
        //     Executes given action on this board.
        // Parameters:
        //     action:
        //         Action to be executed.
        public void DoAction(Action action){
            action.SetBoard(this);
            _actionRecorder.Record(action);
        }

        // Summary:
        //     Rewinds last action.
        public void UndoAction(){
            _actionRecorder.Rewind();
        }

        // Summary:
        //     Checks if a pawn corresponds to given index.
        // Parameters:
        //     index:
        //         Index to be checked.
        public bool IsIndexValid(int index){
            bool result = 
                index >= 0 &&
                index < pawnActivenessArray.Length;
            return result;
        }

        // Summary:
        //     Check if pawn can be moved from given position to other given position.
        // Parameters:
        //     from:
        //         Where is pawn located.
        //     to:
        //         Where is pawn wanted to be moved.
        public bool IsMoveLegal(Vector2Int from, Vector2Int to){
            int fromIndex = BoardPositionToIndex(from); // Corresponding index
            int toIndex = BoardPositionToIndex(to);     // Corresponding index

            // If index is not valid or there is no pawn there
            if(!IsIndexValid(fromIndex) || !_pawnActivenessArray.GetAsBool(fromIndex)){
                return false;
            }

            // If index is not valid or there is pawn there
            if(!IsIndexValid(toIndex) || _pawnActivenessArray.GetAsBool(toIndex)){
                return false;
            }

            Vector2Int difference = to - from;

            // Check if all the positions in between are full
            foreach (Vector2Int pos in Util.WholePointsBetween(from, to)){
                int posIndex = BoardPositionToIndex(pos);

                // If there is empty position in between
                if(!IsIndexValid(posIndex) || !_pawnActivenessArray.GetAsBool(posIndex)){
                    return false;
                }
            }

            Vector2Int differenceAbs = Util.Abs(difference);

            bool result = 
                (differenceAbs.x + differenceAbs.y) == 2 && // If move magnitude is 2 
                (differenceAbs.x * differenceAbs.y) == 0 && // If move is either horizontal or vertical
                boundsChecker.Invoke(from.x, from.y) &&     // If start point is in boundaries
                boundsChecker.Invoke(to.x, to.y);           // If end point is in boundaries

            return result;
        }

        // Summary:
        //     Load given data.
        // Parameters:
        //     boardData:
        //         Data to be loaded.
        public void LoadData(BoardData boardData){
            // Clear arrays
            OnDisable();                        
            _boardActivenessArray.Dispose();

            // Fill arrays with given data
            _pawnActivenessArray = new NativeArray<uint>(boardData.pawnActivenessArray, Allocator.Persistent);
            _boardActivenessArray = new NativeArray<uint>(boardData.boardActivenessArray, Allocator.Persistent);
            playableHoles = new List<int>(boardData.playableHoles);

            // Board status is changed old actions don't apply, clear recorder
            _actionRecorder.Clear();
        }
    }

    // Summary:
    //     Class to keep board data.
    [System.Serializable]
    public class BoardData{

        public uint[] pawnActivenessArray;
        
        public uint[] boardActivenessArray;

        public int[] playableHoles;

        public BoardData(Board board){
            pawnActivenessArray = board.pawnActivenessArray.ToArray();
            boardActivenessArray = board.boardActivenessArray.ToArray();
            playableHoles = board.playableHoles.ToArray();
        }
    }

    // Child classes below are for different board types,
    // only differences are size of the board, start position
    // and bounds checker, further explanation for them
    // is not seen necessary

    [System.Serializable]    
    [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
    class English : Board{
        public override BoundsChecker boundsChecker => IsInBounds;

        public English() : base(7, 7){}

        [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
        static public bool IsInBounds(int x, int y){

            if(x < -3 || x > 3 || y < -3 || y > 3){
                return false;
            }

            if(abs(x) > 1 && abs(y) > 1 && abs(x) < 4 && abs(y) < 4){
                return false;
            }

            return true;
        }
    }

    [System.Serializable]    
    [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
    class European : Board{

        public override BoundsChecker boundsChecker => IsInBounds;

        public European() : base(7, 7, 3, 4){}

        [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
        static public bool IsInBounds(int x, int y){

            if(x < -3 || x > 3 || y < -4 || y > 4){
                return false;
            }

            if(abs(x) + abs(y) > 4){
                return false;
            }

            return true;
        }
    }
    
    [System.Serializable]    
    [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
    class Asymmetrical : Board{

        public override BoundsChecker boundsChecker => IsInBounds;

        public Asymmetrical() : base(8, 8){}

        [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
        static public bool IsInBounds(int x, int y){
            
            if(x < -4 || x > 3 || y < -4 || y > 3){
                return false;
            }

            if(abs(x) > 1 && abs(y) > 1){
                return false;
            }

            return true;
        }
    }
    
    [System.Serializable]    
    [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
    class German : Board{

        public override BoundsChecker boundsChecker => IsInBounds;

        public German() : base(9, 9){}

        [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
        static public bool IsInBounds(int x, int y){
            
            if(x < -4 || x > 4 || y < -4 || y > 4){
                return false;
            }

            if(abs(x) > 1 && abs(y) > 1){
                return false;
            }

            return true;
        }
    }
    
    [System.Serializable]    
    [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
    class Diamond : Board{

        public override BoundsChecker boundsChecker => IsInBounds;

        public Diamond() : base(9, 9){}

        [BurstCompile(FloatPrecision.Standard, FloatMode.Fast)]
        static public bool IsInBounds(int x, int y){
            
            if(x < -4 || x > 4 || y < -4 || y > 4){
                return false;
            }

            if(abs(x) + abs(y) > 4){
                return false;
            }

            return true;
        }
    }


    // Summary:
    //     Thrown when out of board is tried to be reached.
    [System.Serializable]
    public class OutOfBoardException : System.Exception
    {
        public OutOfBoardException() { }
        public OutOfBoardException(string message) : base(message) { }
        public OutOfBoardException(string message, System.Exception inner) : base(message, inner) { }
        protected OutOfBoardException(
            System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext context) : base(info, context) { }
    }

    // Summary:
    //     Thrown when no board save is found.
    [System.Serializable]
    public class NoBoardSaveException : System.Exception
    {
        public NoBoardSaveException() { }
        public NoBoardSaveException(string message) : base(message) { }
        public NoBoardSaveException(string message, System.Exception inner) : base(message, inner) { }
        protected NoBoardSaveException(
            System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext context) : base(info, context) { }
    }
}
