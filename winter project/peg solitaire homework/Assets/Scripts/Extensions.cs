using System.Collections;
using System;
using UnityEngine;
using Unity.Collections;

public static class Extensions {
    public static Vector2 ToVector2(this Vector2Int v){
        return new Vector2(v.x, v.y);
    }

    public static bool GetAsBool(this NativeArray<uint> arr, int index){
        return Convert.ToBoolean(arr[index]); 
    }
}