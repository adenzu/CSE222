using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Unity.Jobs;
using UnityEngine.Events;
using Unity.Burst;
using Unity.Collections;

using static Unity.Mathematics.math;

// 
// Summary:
//     Solitaire is the class that handles the game.
[RequireComponent(typeof(AudioSource))]
public class Solitaire : MonoBehaviour{

    // Summary:
    //     Undo button gameobject.    
    [SerializeField]
    GameObject undoButton;

    // Summary:
    //     Mesh for rendering.
    [SerializeField]
    Mesh pawnMesh, boardMesh;

    // Summary:
    //     Material for rendering.
    [SerializeField]
    Material pawnMaterial, boardMaterial;

    // Summary:
    //     Distance between two pawns.
    [SerializeField, Min(0)]
    private float _stepSize;
    public float stepSize => _stepSize;

    // Summary:
    //     Size of the pawns.
    [SerializeField, Min(0)]
    private float _pawnSize;

    // Summary:
    //     Current board.
    [SerializeField]
    BoardLibrary.BoardType boardType;

    // Summary:
    //     Sound effects.
    [SerializeField]
    AudioClip selectionSFX, moveSFX, undoSFX;

    // Summary:
    //     Sound effect player.
    AudioSource audioSource;

    // Summary:
    //     Is a pawn selected.
    private bool _pawnSelected = false;
    public bool pawnSelected => _pawnSelected;

    // Summary:
    //     Board position of the selected pawn. 
    Vector2Int selectedPawnBoardPosition;

    // Summary:
    //     Current board object.
    public BoardLibrary.Board board => BoardLibrary.GetBoard(boardType);

    // Summary:
    //     Pawn hashes for procedural rendering. One uint for every pawn, 1 for render 0 for don't.
    NativeArray<uint> pawnHashes => board.pawnActivenessArray;

    // Summary:
    //     Board hashes for procedural rendering. One uint for every board unit, 1 for render 0 for don't.
    NativeArray<uint> boardHashes => board.boardActivenessArray;

    // Summary:
    //     Buffers for procedural rendering.
    ComputeBuffer pawnHashesBuffer, boardHashesBuffer;

    // Summary:
    //     PropertyBlocks for procedural rendering. Handles value transfer to shader.
    MaterialPropertyBlock pawnPropertyBlock, boardPropertyBlock;
    
    // Summary:
    //     Set up variables for procedural rendering.
    private void OnEnable() {
        // Size of compute buffer, which has to be same size as hashes which are the size of the board.
        int size = board.width * board.height;

        // Earlier computations of board size related parameters that change the rendered final product. 
        Vector4 config = new Vector4(board.width, board.width / 2, board.height / 2, 1f / board.width);

        // Set up procedural render related variables.
        UpdatePawnsMesh(size, config);
        UpdateBoardMesh(size, config);
    }

    // Summary:
    //     Deallocation of buffers.
    void OnDisable () {
        pawnHashesBuffer.Release();
        pawnHashesBuffer = null;
        
        boardHashesBuffer.Dispose();
        boardHashesBuffer = null;
    }

    // Summary:
    //     Rerenders board.
    void OnValidate () {
        if(pawnHashesBuffer != null && boardHashesBuffer != null && enabled) {
            OnDisable();
            OnEnable();
        }
    }
    
    // Summary:
    //     Sets up pawn related render variables.
    // Parameters:
    //     size:
    //         Size of the board.
    //     config:
    //         Vector4 with values board width as x, half of board width as y, half of board height as z, and inverse of width as w.
    void UpdatePawnsMesh(int size, Vector4 config){
        // Compute buffer.
        pawnHashesBuffer = new ComputeBuffer(size, sizeof(uint));

        // Set data.
        pawnHashesBuffer.SetData(pawnHashes);

        // If property block is not initialized do so.
        pawnPropertyBlock ??= new MaterialPropertyBlock();

        // Set values for shader to use.
        pawnPropertyBlock.SetBuffer("_Hashes", pawnHashesBuffer);
        pawnPropertyBlock.SetFloat("_StepSize", _stepSize);
        pawnPropertyBlock.SetFloat("_PawnSize", _pawnSize);
        pawnPropertyBlock.SetVector("_Config", config);
    }

    // Summary:
    //     Sets up board related render variables.
    // Parameters:
    //     size:
    //         Size of the board.
    //     config:
    //         Vector4 with values board width as x, half of board width as y, half of board height as z, and inverse of width as w.
    void UpdateBoardMesh(int size, Vector4 config){
        // Compute buffer.
        boardHashesBuffer = new ComputeBuffer(size, sizeof(uint));

        // Set data.
        boardHashesBuffer.SetData(boardHashes);

        // If property block is not initialized do so.
        boardPropertyBlock ??= new MaterialPropertyBlock();

        // Set values for shader to use.
        boardPropertyBlock.SetBuffer("_Hashes", boardHashesBuffer);
        boardPropertyBlock.SetFloat("_StepSize", _stepSize);
        boardPropertyBlock.SetFloat("_PawnSize", _pawnSize);
        boardPropertyBlock.SetVector("_Config", config);
    }

    private void Start() {
        // Get audio source component.
        audioSource = GetComponent<AudioSource>();
    }

	private void Update() {
        // Render board.
        Bounds bounds = new Bounds(Vector3.zero, new Vector2(board.width, board.height));
		Graphics.DrawMeshInstancedProcedural(pawnMesh, 0, pawnMaterial, bounds, pawnHashes.Length, pawnPropertyBlock);
		Graphics.DrawMeshInstancedProcedural(boardMesh, 0, boardMaterial, bounds, boardHashes.Length, boardPropertyBlock);
	}

    // Summary:
    //     Sets the current board.
    // Parameters:
    //     name:
    //         Name/type of the board.
    public void SetBoard(BoardLibrary.BoardType name){
        boardType = name;       
        _pawnSelected = false;  // Reset pawn selection.
        OnValidate();           // Render new board.
    }

    // Summary:
    //     Move pawn to given world position.
    // Parameters:
    //     to:
    //         World position where pawn is wanted to be moved.
    public void MovePawn(Vector3 to){
        MovePawn(WorldToBoard(to));
    }

    // Summary:
    //     Move pawn to given board position.
    // Parameters:
    //     to:
    //         Board position where pawn is wanted to be moved.
    public void MovePawn(Vector2Int to){

        // Distance between
        Vector2Int differenceAbs = Util.Abs(to - selectedPawnBoardPosition);

        // Index of possible new location
        int pawnIndex = board.BoardPositionToIndex(to);

        // If move is legal
        if(board.IsMoveLegal(selectedPawnBoardPosition, to)){
            board.DoAction(new Move(selectedPawnBoardPosition, to));    // Move pawn
            OnValidate();
            undoButton.SetActive(true);                                 // New move has been done so set undo button active
            audioSource.PlayOneShot(moveSFX);   
        }
        // If move is illegal but
        else if(
            board.IsIndexValid(pawnIndex) &&    // Position is inside board limits
            board.GetActive(pawnIndex) &&       // There is an active pawn at position
            board.boundsChecker(to.x, to.y)     // Position is inside board boundaries
        )
        {
            SelectPawnAt(pawnIndex);    // Then select the pawn at the move position 
            return;
        }

        // If neither then Unselect pawn
        UnselectPawn();
    }

    // Summary:
    //     Undoes the last move.
    public void Undo(){
        try
        {
            board.UndoAction();
            OnValidate();
            audioSource.PlayOneShot(undoSFX);
            if(board.actionRecorder.actionCount == 0){  // If there's no recorded move left 
                undoButton.SetActive(false);            // Deactivate undo button
            }
        }
        catch (ActionRecorder.NoActionException)
        {
            // pass
        }
    }

    // Summary:
    //     Saves current board type.
    public void SaveBoard(){
        BoardLibrary.SaveBoard(boardType);
    }

    // Summary:
    //     Loads current board type.
    public void LoadBoard(){
        try
        {
            BoardLibrary.LoadBoard(boardType);
            OnValidate();
        }
        catch(BoardLibrary.NoBoardSaveException)
        {
            // pass
        }
    }

    // Summary:
    //     Resets current board.
    public void ResetBoard(){
        board.Reset();
        OnValidate();
    }

    // Summary:
    //     Transforms world position to board position.
    // Parameters:
    //     position:
    //         World position that will be casted to board position.
    Vector2Int WorldToBoard(Vector3 position){
        position /= _stepSize;
        return new Vector2Int(Mathf.RoundToInt(position.x), Mathf.RoundToInt(position.z));
    }

    // Summary:
    //     Sets selected pawn index for shader.
    // Parameters:
    //     pawnIndex:
    //         Index of pawn to be selected.
    void SetSelectedPawn(int pawnIndex){
        pawnPropertyBlock.SetInt("_Selected", pawnIndex);
    }

    // Summary:
    //     Selects pawn at given world position.
    // Parameters:
    //     position:
    //         World position of the pawn that is to be selected.
    public void SelectPawnAt(Vector3 position){
        SelectPawnAt(WorldToBoard(position));
    }
    
    // Summary:
    //     Selects pawn at given board position.
    // Parameters:
    //     position:
    //         Board position of the pawn that is to be selected.
    public void SelectPawnAt(Vector2Int boardPosition){
        SelectPawnAt(board.BoardPositionToIndex(boardPosition), boardPosition);
    }
    
    // Summary:
    //     Selects pawn with given index.
    // Parameters:
    //     pawnIndex:
    //         Index of the pawn that is to be selected.
    //     _boardPosition:
    //         Board position of the pawn that is to be selected.
    public void SelectPawnAt(int pawnIndex, Vector2Int? _boardPosition = null){
        _boardPosition ??= board.IndexToBoardPosition(pawnIndex);
        Vector2Int boardPosition = (Vector2Int) _boardPosition;

        if(!board.boundsChecker(boardPosition.x, boardPosition.y)){
            return;
        }

        selectedPawnBoardPosition = boardPosition;

        if(
            pawnIndex > -1 && pawnIndex < board.width * board.height &&
            board.GetActive(pawnIndex)
        )
        {
            SetSelectedPawn(pawnIndex);
            audioSource.PlayOneShot(selectionSFX);
            _pawnSelected = true;
        }
        else{
            UnselectPawn();
        }
    }

    // Summary:
    //     Unselects the selected pawn.
    void UnselectPawn(){
        SetSelectedPawn(board.width * board.height);
        _pawnSelected = false;
    }
}

