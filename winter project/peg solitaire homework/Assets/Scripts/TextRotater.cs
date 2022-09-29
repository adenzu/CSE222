using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

// Summary:
//     Class to handle repetitive text changes. 
public class TextRotater : MonoBehaviour
{
    // Summary:
    //     Text object to change.
    [SerializeField]
    TextMeshProUGUI text;

    // Summary:
    //     Texts.
    [SerializeField]
    string[] texts;

    // Summary:
    //     Current text's index
    int textIndex = 0;

    // Summary:
    //     Updates text as the next one, gets to start text if no other text is left.
    public void RotateText(){
        textIndex = (textIndex + 1) % texts.Length;
        text.text = texts[textIndex];
    }
}
