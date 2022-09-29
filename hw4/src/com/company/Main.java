package com.company;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Main class
 */
public class Main {

    /**
     * Main func
     * @param args  Command line args
     */
    public static void main(String[] args) {
        driverFunc();
    }

    static void driverFunc() {
        Scanner scanner = new Scanner(System.in);

        print("Enter to start...");
        scanner.nextLine();

        print("Recursive string search function test cases:\n");
        testFindString("First test", "test", 1);
        testFindString("Second text", "test", 1);
        testFindString("Third test of tests", "test", 1);
        testFindString("Fourth test of tests", "test", 2);
        testFindString("Fifth test of tests", "test", 3);
        testFindString("This one", "has no occurrence", 0);
        testFindString("", "No text to search in", 1);
        testFindString("No text to search", "", 1);
        testFindString("No text to search", "", 0);
        testFindString("Can negative occurrence", "occur", -1);

        print("Enter to continue...");
        scanner.nextLine();

        print("Recursive integer counting in given array between given values:\n");
        testCountInBetween(new int[]{1,2,3,4,5,6}, 2, 5);
        testCountInBetween(new int[]{1,2,3,4,5,6}, 1, 2);
        testCountInBetween(new int[]{1,2,3,4,5,6}, 1, 7);
        testCountInBetween(new int[]{1,2,3,4,5,6}, 0, 2);
        testCountInBetween(new int[]{1,2,3,4,5,6}, 0, 7);
        testCountInBetween(new int[]{1,2,3,4,5,6}, 5, 2);
        testCountInBetween(new int[]{1,2,3,4,5,6}, 5, 5);
        testCountInBetween(new int[]{1,1,1,1,1,2,3,3,3,3,3,3,3,5,5,5,5,5,6}, 2, 5);
        testCountInBetween(new int[]{1,1,1,1,1,2,3,3,3,3,3,3,3,5,5,5,5,5,6}, 1, 2);
        testCountInBetween(new int[]{1,1,1,1,1,2,3,3,3,3,3,3,3,5,5,5,5,5,6}, 1, 3);
        testCountInBetween(new int[]{1,1,1,1,1,2,3,3,3,3,3,3,3,5,5,5,5,5,6}, 3, 3);

        print("Enter to continue...");
        scanner.nextLine();

        print("Recursive subarray with given sum finding:\n");
        testFindSummingSubarrays(new int[]{1,2,3,4,3,3,2,1,4}, 10);
        testFindSummingSubarrays(new int[]{1,2,3,4,3,3,2,1,4}, 0);
        testFindSummingSubarrays(new int[]{-2,-1,0,1,2}, 0);
        testFindSummingSubarrays(new int[]{1,2,3,-6,4,5,1,6}, 10);
        testFindSummingSubarrays(new int[]{0,0,0,0}, 0);
        testFindSummingSubarrays(new int[]{-1,2,-3,4,-5,6,-7}, 2);
        testFindSummingSubarrays(new int[]{-1,2,-3,4,-5,6,-7}, 1);
        testFindSummingSubarrays(new int[]{-1,2,-3,4,-5,6,-7}, -1);
        testFindSummingSubarrays(new int[]{-1,2,-3,4,-5,6,-7}, -2);

        print("Enter to continue...");
        scanner.nextLine();

        print("Recursive array filling:\n");
        testPrintArrayFillings(0);
        testPrintArrayFillings(1);
        testPrintArrayFillings(2);
        testPrintArrayFillings(3);
        testPrintArrayFillings(7);
        testPrintArrayFillings(10);

        print("Enter to continue...");
        scanner.nextLine();

        print("Filling a 2x2 matrix with snakes:");
        RecursiveFunctions.printMatrixFillings(2);

        print("Enter to continue...");
        scanner.nextLine();

        print("Filling a 5x5 matrix with snakes:");
        RecursiveFunctions.printMatrixFillings(5);
    }

    static void testFindString(String searchedIn, String searched, int occurrence) {
        print("Searched in: " + searchedIn);
        print("Searched text: " + searched);
        print("Occurrence: " + occurrence);
        print("Result:");
        print(RecursiveFunctions.findString(searchedIn, searched, occurrence));
        print();
    }

    static void testCountInBetween(int[] arr, int floor, int ceil) {
        print("Array: " + Arrays.toString(arr));
        print("Between: " + floor + "-" + ceil);
        print("Result:");
        print(RecursiveFunctions.countInBetween(arr, floor, ceil));
        print();
    }

    static void testFindSummingSubarrays(int[] arr, int sum) {
        print("Array: " + Arrays.toString(arr));
        print("Sum: " + sum);
        print("Result:");
        print(RecursiveFunctions.findSummingSubarrays(arr, sum));
        print();
    }

    static void testPrintArrayFillings(int length) {
        print("For array with length of " + length);
        print("Result:");
        RecursiveFunctions.printArrayFillings(length);
        print();
    }

    static void print() {
        System.out.println();
    }

    static void print(Object o) {
        System.out.println(o);
    }
}
