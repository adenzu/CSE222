using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Summary:
//     Utility class.
public static class Util 
{
    public static ulong GCD(ulong a, ulong b){
        while (a != 0 && b != 0)
        {
            if (a > b)
                a %= b;
            else
                b %= a;
        }

        return a | b;
    }

    public static Vector2Int Abs(Vector2Int vec){
        return new Vector2Int(Mathf.Abs(vec.x), Mathf.Abs(vec.y));
    }

    // Summary:
    //     Returns positions between given positions that have same slope and are fractionless.
    // Parameters:
    //     from:
    //         One of the ends.
    //     to:
    //         Other one of the ends.
    public static Vector2Int[] WholePointsBetween(Vector2Int from, Vector2Int to){
        Vector2Int difference = to - from;
        Vector2Int differenceAbs = Util.Abs(difference);

        int gcd = (int) Util.GCD((ulong) differenceAbs.x, (ulong) differenceAbs.y);

        if(gcd > 1){
            Vector2Int[] result = new Vector2Int[gcd - 1];  // End positions are excluded thus length of gcd - 1

            Vector2Int step = difference / gcd;             // Unit vector with same slope

            for(int i = 1; i < gcd; ++i){                   
                result[i - 1] = step * i + from;
            }

            return result;
        }

        return new Vector2Int[]{};
    }
}
