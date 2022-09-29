using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Summary:
//     Class for positioning the camera.
public class CameraPositioner : MonoBehaviour
{

    [SerializeField]
    Transform solitaireTransform;

    Solitaire solitaire;

    const float sqrt2 = 1.4142135623730951f;

    private void Awake() {
        transform.localRotation = Quaternion.Euler(60, 0, 0);       // Rotate camera 
        solitaire = solitaireTransform.GetComponent<Solitaire>();
    }

    // Summary:
    //     Positions the camera so that board can be seen.
    // Parameters:
    //     boardIndex:
    //         Index of the board.
    public void PositionToBoard(int boardIndex){
        BoardLibrary.Board board = BoardLibrary.GetBoard((BoardLibrary.BoardType) boardIndex);

        float size = Mathf.Max(board.width, board.height) * solitaire.stepSize;

        Vector3 position = new Vector3(0, 0.5f * sqrt2 * size, -size * 0.5f - 1.5f);
        transform.position = solitaireTransform.position + position;
    }
}
