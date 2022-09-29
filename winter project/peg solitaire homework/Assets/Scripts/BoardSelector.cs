using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Summary:
//     Class for board type selection button.
public class BoardSelector : MonoBehaviour
{
    [SerializeField]
    Solitaire solitaire;

    BoardLibrary.BoardType boardType;

    public void SetBoard(int index){
        solitaire.SetBoard((BoardLibrary.BoardType) index);
    }
}
