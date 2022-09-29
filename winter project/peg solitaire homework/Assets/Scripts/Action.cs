using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Summary:
//     Inteface of an action.
public interface ActionInterface 
{
    // Summary:
    //     Executes action.
    public void Do();

    // Summary:
    //     Reverses the executed action.
    public void Undo();
}

// Summary:
//     Action base class.
public abstract class Action : ActionInterface{

    // Summary:
    //     The board where the action is executed on.
    private BoardLibrary.Board _board;
    protected BoardLibrary.Board board => _board;

    // Summary:
    //     Creates action with no board attached.
    public Action(){
    }

    // Summary:
    //     Creates action attached to given board.
    // Parameters:
    //     board:
    //         The board that is to be attached. 
    public Action(BoardLibrary.Board board){
        SetBoard(board);
    }

    public abstract void Do();
    public abstract void Undo();

    // Summary:
    //     Sets attached board.
    // Parameters:
    //     board:
    //         The board that is to be attached. 
    public void SetBoard(BoardLibrary.Board board){
        _board = board;
    }
}

// Summary:
//     Action class that holds the information of a move and has related functionality.
public class Move : Action{

    // Summary:
    //     Two endpoints of the move.
    private Vector2Int _from, _to;

    // Summary:
    //     Creates a move object with no board attached.
    // Parameters:
    //     from:
    //         The position of the pawn that is moved.
    //     to:
    //         The destination of the moved pawn.
    public Move(Vector2Int from, Vector2Int to){
        _from = from;
        _to = to;
    }

    // Summary:
    //     Creates a move object attached to given board.
    // Parameters:
    //     from:
    //         The position of the pawn that is moved.
    //     to:
    //         The destination of the moved pawn.
    //     board:
    //         The board which the move object is attached to.
    public Move(Vector2Int from, Vector2Int to, BoardLibrary.Board board) : base(board){
        _from = from;
        _to = to;
    }

    public override void Do(){
        int fromIndex = board.BoardPositionToIndex(_from);                  // Index of older position
        board.SetActive(fromIndex, false);                                  // Moved pawn is no longer rendered at older position
        board.playableHoles.Add(fromIndex);                                 // Since it's empty now may be a hole that another pawn can move to

        foreach (Vector2Int pos in Util.WholePointsBetween(_from, _to)){    // For every position in between
            int posIndex = board.BoardPositionToIndex(pos);                 // Index of the position
            board.SetActive(posIndex, false);                               // Pawn at position is no longer rendered
            board.playableHoles.Add(posIndex);                              // May be a hole that another pawn can move to
        }

        int toIndex = board.BoardPositionToIndex(_to);                      // Index of new position
        board.SetActive(toIndex, true);                                     // Pawn is rendered at its new
        board.playableHoles.Remove(toIndex);                                // Position is no longer can be moved to
    }

    public override void Undo(){
        int toIndex = board.BoardPositionToIndex(_to);                      // Index of destination position
        board.SetActive(toIndex, false);                                    // Moved pawn is no longer rendered at destination
        board.playableHoles.Add(toIndex);                                   // Since it's empty now may be a hole that another pawn can move to

        foreach (Vector2Int pos in Util.WholePointsBetween(_to, _from)){    // For every position in between
            int posIndex = board.BoardPositionToIndex(pos);                 // Index of the position
            board.SetActive(posIndex, true);                                // Pawn at position is no longer rendered
            board.playableHoles.Remove(posIndex);                           // Since not empty no longer a pawn can be moved to
        }

        int fromIndex = board.BoardPositionToIndex(_from);                  // Index of position before move
        board.SetActive(fromIndex, true);                                   // Pawn is rendered at its old position
        board.playableHoles.Remove(fromIndex);                              // Position is no longer can be moved to
    }
}
