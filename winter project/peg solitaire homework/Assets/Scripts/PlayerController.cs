using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Summary:
//     Class to handle player control.
public class PlayerController : MonoBehaviour
{

    // Summary:
    //     Played game.
    [SerializeField]
    Solitaire solitaire;

    // Summary:
    //     Plane where pawns and board are on.
    Plane plane = new Plane(Vector3.up, 0);

    // Summary:
    //     Last clicked world position.
    Vector3 clickedPosition = Vector3.zero;

    void Update(){
        if(Input.GetMouseButtonDown(0)){
            GetClickedPosition();
            if(solitaire.pawnSelected){
                solitaire.MovePawn(clickedPosition);
            }
            else{
                solitaire.SelectPawnAt(clickedPosition);
            }
        }

        if(Input.GetKeyDown(KeyCode.Escape)){
            Application.Quit();
        }
    }

    // Summary:
    //     Updates clicked position.
    void GetClickedPosition(){
        Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
        float distance;

        if(plane.Raycast(ray, out distance)){
            clickedPosition = ray.GetPoint(distance);
        }
    }
}
