using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Summary:
//     Class for auto play.
public class Computer : MonoBehaviour
{
    // Summary:
    //     Game to be played.
    [SerializeField]
    Solitaire solitaire;

    // Summary:
    //     Cooldown between moves.
    [SerializeField]
    float timeBetween;

    // Summary:
    //     How long has it been since last move.
    float sinceLastMove;

    // Summary:
    //     Random Number Generator.
    System.Random rng;

    // Summary:
    //     Board where game is played on.
    BoardLibrary.Board board => solitaire.board;

    // Summary:
    //     Valid positions to play.
    List<int> validTargets => board.playableHoles;

    // Summary:
    //     Move directions.
    Vector2Int[] directions = new Vector2Int[]{Vector2Int.up, Vector2Int.down, Vector2Int.right, Vector2Int.left};

    // Summary:
    //     Play without stopping.
    bool playConsecutively = false;

    private void Start() {
        rng = new System.Random();
    }

    // Summary:
    //     Plays a random move.
    public void Play(){
        // List of invalid targets
        List<int> invalids = new List<int>();

        for(int i = 0; i < validTargets.Count; ++i){
            int toIndex = validTargets[i];
            bool isValid = false;

            foreach(int directionIndex in GetDirectionValidation(toIndex)){
                if(directionIndex == -1){
                    continue;               // Cant move in this direction check next one
                }
                else{
                    Vector2Int to = board.IndexToBoardPosition(toIndex);
                    Vector2Int from = to - directions[directionIndex % 4] * 2;

                    solitaire.SelectPawnAt(from);
                    solitaire.MovePawn(to);

                    isValid = true;

                    // Check for new targets that are available after this move
                    foreach(Vector2Int direction in directions){
                        Vector2Int pos = to + direction;

                        foreach(int checkIndex in GetDirectionValidation(pos)){
                            if(checkIndex == -1){
                                continue;
                            }
                            else{
                                validTargets.Add(board.BoardPositionToIndex(pos));
                            }
                        }
                    }

                    break;
                }
            }

            if(isValid){    // If valid then the move is executed
                break;      // no need for further checks
            }
            else{                       
                invalids.Add(toIndex);
            }
        }

        // Remove invalid positions
        foreach(int index in invalids){
            validTargets.Remove(index);
        }
    }

    // Summary:
    //     Checks if a move can be done towards given index. Returns for each direction.
    // Parameters:
    //     index:
    //         Index of the board position to be moved to.
    IEnumerable<int> GetDirectionValidation(int index){
        return GetDirectionValidation(board.IndexToBoardPosition(index));
    }

    // Summary:
    //     Checks if a move can be done towards given index. Returns for each direction.
    // Parameters:
    //     to:
    //         Board position to be moved to.
    IEnumerable<int> GetDirectionValidation(Vector2Int to){

        // Random direction index
        int direction = rng.Next(0, 4);

        for(int i = 0; i < 4; ++i, ++direction){
            Vector2Int from = to - directions[direction % 4] * 2;   // Set from
            
            if(board.IsMoveLegal(from, to)){    // If legal
                yield return direction % 4;     // return index of the direction
            }
        }

        yield return -1;                        // If no direction is valid then return -1
    }

    public void ToggleConsecutivePlay() {
        playConsecutively = !playConsecutively;
    }

    private void Update() {
        sinceLastMove += Time.deltaTime;
        if(playConsecutively){
            if(sinceLastMove > timeBetween){
                Play();
                sinceLastMove = 0;
            }
        }
    }
}
