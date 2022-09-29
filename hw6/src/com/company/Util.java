package com.company;

/**
 * Class consisting of unrelated functions that are useful.
 */
public class Util {
    /**
     * Finds the greatest prime that is less than given positive integer.
     * @param n Upper bound for the prime.
     * @return  Prime that is less than n and bigger than any other prime less than n that is not it.
     */
    public static long findPrimeLessNearest(long n) {
        // Make n odd
        n -= (n + 1) % 2;

        // Divisor
        long j;
        for(long i = n; i > 2; i -= 2){
            for(j = 3; j <= Math.sqrt(i); j += 2){
                if(i % j == 0) break;
            }

            if(j > Math.sqrt(i)) return i;
        }

        return 2;
    }
}
