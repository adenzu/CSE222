using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

// Summary:
//     Class for executing functions when app is quitted.
public class OnQuitExecuter : MonoBehaviour
{
    // Summary:
    //     Type of functions that can be executed when app is quitted.
    public delegate void ExecutedOnQuit();

    // Summary:
    //     List of functions that will be executed on quit.
    private static List<ExecutedOnQuit> executedOnQuits = new List<ExecutedOnQuit>();

    private void OnApplicationQuit() {
        foreach(ExecutedOnQuit func in executedOnQuits){
            func.Invoke();
        }
    }

    public static void AddFuncToExecuteOnQuit(ExecutedOnQuit func){
        executedOnQuits.Add(func);
    }
    
    public static void RemoveFuncToExecuteOnQuit(ExecutedOnQuit func){
        executedOnQuits.Remove(func);
    }
}
