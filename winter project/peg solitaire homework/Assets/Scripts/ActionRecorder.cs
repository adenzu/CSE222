using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Summary:
//     Records actions.
public class ActionRecorder{

    // Summary:
    //     Where executed actions are stored.
    private Stack<ActionInterface> actions = new Stack<ActionInterface>();

    // Summary:
    //     Count of remaining actions.
    public int actionCount => actions.Count; 

    // Summary:
    //     Executes and records given action.
    // Parameters:
    //     action:
    //         Action to be executed and recorded.
    public void Record(ActionInterface action){
        action.Do();
        actions.Push(action);
    }

    // Summary:
    //     Rewinds last action.
    public void Rewind(){
        if(actions.Count == 0){
            throw new NoActionException();
        }
        actions.Pop().Undo();
    }

    // Summary:
    //     Clears action record.
    public void Clear(){
        actions.Clear();
    }

    // Summary:
    //     Exception that occurs when there is no recorded action to rewind.
    [System.Serializable]
    public class NoActionException : System.Exception
    {
        public NoActionException() { }
        public NoActionException(string message) : base(message) { }
        public NoActionException(string message, System.Exception inner) : base(message, inner) { }
        protected NoActionException(
            System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext context) : base(info, context) { }
    }
}
