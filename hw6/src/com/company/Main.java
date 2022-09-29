package com.company;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main class
 */
public class Main {

    /**
     * Main function.
     * @param args  Console arguments.
     */
    public static void main(String[] args) {
        driverFunc();
    }

    private static void driverFunc() {
        hashmapTests(100, 100);
        hashmapTests(100, 1_000);
        hashmapTests(100, 10_000);

        sortTests(1_000, 100);
        sortTests(1_000, 1_000);
        sortTests(1_000, 10_000);
    }

    private static void sortTests(int testCount, int arraySize) {
        print("Starting sorting tests...");
        print("There will be " + testCount + " many tests with arrays size of " + arraySize);

        long mergeSortSum = 0;
        long quickSortSum = 0;
        long newSortSum = 0;

        for(int i = 0; i < testCount; ++i){
            Integer[] randomArray = new Integer[arraySize];
            Arrays.setAll(randomArray, k -> ThreadLocalRandom.current().nextInt());

            long start = System.currentTimeMillis();
            SortingMethods.MergeSort(randomArray.clone());
            mergeSortSum += System.currentTimeMillis() - start;

            start = System.currentTimeMillis();
            SortingMethods.QuickSort(randomArray.clone());
            quickSortSum += System.currentTimeMillis() - start;

            start = System.currentTimeMillis();
            SortingMethods.NewSort(randomArray.clone());
            newSortSum += System.currentTimeMillis() - start;
        }

        print("Mergesort took " + (float) mergeSortSum / testCount + " milliseconds on average.");
        print("Quicksort took " + (float) quickSortSum / testCount + " milliseconds on average.");
        print("Newsort took " + (float) newSortSum / testCount + " milliseconds on average.\n");
    }

    private static void hashmapTests(int testCount, int mapSize) {
        print("Starting test with " + testCount + " number of tries and with hashmaps size of " + mapSize);

        float chainedPutSum = 0;
        float hybridPutSum  = 0;

        float chainedGetSucSum = 0;
        float hybridGetSucSum  = 0;

        float chainedRemoveSucSum = 0;
        float hybridRemoveSucSum  = 0;

        float chainedGetUnsucSum = 0;
        float hybridGetUnsucSum  = 0;

        float chainedRemoveUnsucSum = 0;
        float hybridRemoveUnsucSum  = 0;

        for(int i = 0; i < testCount; ++i){
            int[] randomInts = new int[mapSize];
            Arrays.setAll(randomInts, k -> ThreadLocalRandom.current().nextInt(0, 100 * mapSize));

            HashMapChain<Integer, Integer> hashMapChain = new HashMapChain<>();

            long start = System.nanoTime();
            for(int j : randomInts){
                hashMapChain.put(j, j);
            }
            chainedPutSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapChain.get(-1);
            }
            chainedGetUnsucSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapChain.get(j);
            }
            chainedGetSucSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapChain.remove(-1);
            }
            chainedRemoveUnsucSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapChain.remove(j);
            }
            chainedRemoveSucSum += (float) (System.nanoTime() - start) / mapSize;

            HashMapHybrid<Integer, Integer> hashMapHybrid = new HashMapHybrid<>();

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapHybrid.put(j, j);
            }
            hybridPutSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapHybrid.get(-1);
            }
            hybridGetUnsucSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapHybrid.get(j);
            }
            hybridGetSucSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapHybrid.remove(-1);
            }
            hybridRemoveUnsucSum += (float) (System.nanoTime() - start) / mapSize;

            start = System.nanoTime();
            for(int j : randomInts){
                hashMapHybrid.remove(j);
            }
            hybridRemoveSucSum += (float) (System.nanoTime() - start) / mapSize;
        }

        print("It took " + chainedPutSum / testCount + " nanoseconds on average to put " + mapSize + " entries to chained hashmap.");
        print("It took " + hybridPutSum / testCount + " nanoseconds on average to put " + mapSize + " entries to hybrid hashmap.\n");

        print("It took " + chainedGetSucSum / testCount + " nanoseconds on average to successfully get " + mapSize + " entries from chained hashmap.");
        print("It took " + hybridGetSucSum / testCount + " nanoseconds on average to successfully get " + mapSize + " entries from hybrid hashmap.\n");

        print("It took " + chainedRemoveSucSum / testCount + " nanoseconds on average to successfully remove " + mapSize + " entries from chained hashmap.");
        print("It took " + hybridRemoveSucSum / testCount + " nanoseconds on average to successfully remove " + mapSize + " entries from hybrid hashmap.\n");

        print("It took " + chainedGetUnsucSum / testCount + " nanoseconds on average to unsuccessfully get " + mapSize + " entries from chained hashmap.");
        print("It took " + hybridGetUnsucSum / testCount + " nanoseconds on average to unsuccessfully get " + mapSize + " entries from hybrid hashmap.\n");

        print("It took " + chainedRemoveUnsucSum / testCount + " nanoseconds on average to unsuccessfully remove " + mapSize + " entries from chained hashmap.");
        print("It took " + hybridRemoveUnsucSum / testCount + " nanoseconds on average to unsuccessfully remove " + mapSize + " entries from hybrid hashmap.\n\n");
    }

    private static void print() {System.out.println();}

    private static void print(Object o) {
        System.out.println(o);
    }
}
