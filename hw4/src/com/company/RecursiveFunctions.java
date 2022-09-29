package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class of various recursive functions. Functions are not exactly related.
 */
public class RecursiveFunctions {

    /**
     * Searches given string in the given other string. Returns the index of the first letter
     * of the text that is searched when it's found.
     * @param searchedIn    The text that <code>searched</code> is searched in.
     * @param searched      The text that is searched.
     * @param occurrence    Number of times the given text has to be found to return its index.
     * @return              If given text is found, the index of it's first letter in the text
     *                      that was searched in. -1 otherwise.
     */
    public static int findString(String searchedIn, String searched, int occurrence) {
        return findString(searchedIn, searched, occurrence, 0, 0);
    }

    /**
     * Counts the number of integers there are in between given two integers in the given
     * ordered integer array.
     * @param arr   Ordered integer array.
     * @param floor Lesser one of limiting integers.
     * @param ceil  Greater one of the limiting integers.
     * @return      If <code>ceil</code> is greater than <code>floor</code> then number of integers
     *              between them <strong>excluding</strong> themselves in the array is returned. If <code>ceil</code>
     *              is lesser than <code>floor</code> then negative of number of integers between them <strong>including</strong>
     *              themselves. If they are equal or not found in the array -1.
     */
    public static int countInBetween(int[] arr, int floor, int ceil) {
        return countInBetween(arr, floor, ceil, 0, arr.length - 1);
    }

    /**
     * Searches for integer lists whose sum is equal to given <code>sum</code> that are
     * continuously found in given array.
     * @param arr   Array that subarray of integers whose sum is equal to given <code>sum</code>
     *              are searched in continuously.
     * @param sum   What shall continuous valid subarrays shall return upon adding up all of their elements.
     * @return      List of integer lists whose sum is equal to given <code>sum</code> that are
     *              continuously found in given array.
     */
    public static List<List<Integer>> findSummingSubarrays(int[] arr, int sum) {
        return findSummingSubarrays(arr, sum, 0, false);
    }

    /**
     * Prints all possible distinct fillings of a 1D array with given size
     * with spacing every filling block one element between them.
     * @param size  Size of the array.
     */
    public static void printArrayFillings(int size){
        for(StringBuilder stringBuilder : printArrayFillings(size, 0, 3, 0)){
            System.out.println(stringBuilder.toString());
        }
    }

    /**
     * Prints all possible distinct fillings of a square matrix with side length of
     * given integer with snakes of length same as side length that can bend
     * only vertical or lateral.
     * @param sideLength    Side length of the square matrix.
     */
    public static void printMatrixFillings(int sideLength) {
        printMatrixFillings(new int[sideLength][sideLength], 0, 0, sideLength * sideLength - 1);
    }

    /**
     * Searches text in another text recursively.
     * @param searchedIn    The text that is searched text is searched in.
     * @param searched      The text that is searched.
     * @param occurrence    Occurrence of the searched text in the other text whose index will be returned.
     * @param i             Index of current character in the <code>searchedIn</code>.
     * @param j             Index of current character in the <code>searched</code>
     * @return              If given text is found, the index of it's first letter in the text that was searched in. -1 otherwise.
     */
    private static int findString(String searchedIn, String searched, int occurrence, int i, int j) {
        // The text wasn't found as many times as it was wanted to be.
        if(occurrence < 1) return -1;

        // Searched text is fully traversed, therefore found
        else if(searched.length() == j){
            // If this match was wanted
            if(occurrence == 1) return i - j;
            else return findString(searchedIn, searched, occurrence - 1, i + 1, 0);
        }

        // Text that was searched in is fully traversed, therefore no
        // text is lef to search in
        else if(searchedIn.length() == i) return -1;

        // If characters of bot texts match
        else if(searchedIn.charAt(i) == searched.charAt(j)){
            // Get to next characters
            return findString(searchedIn, searched, occurrence, i + 1, j + 1);
        }

        // If first character of searched text is found
        // Note:
        //      This has to be checked separately since such search is possible as
        //      findString("aabc", "abc")
        else if(searchedIn.charAt(i) == searched.charAt(0)){
            return findString(searchedIn, searched, occurrence, i + 1, 1);
        }

        // Check the next character
        return findString(searchedIn, searched, occurrence, i + 1, 0);
    }

    /**
     * Counts number of integers between given two integers in given sorted array.
     * @param arr   Sorted array.
     * @param floor Lesser of the limiting integers.
     * @param ceil  Greater of the limiting integers.
     * @param first Relative index of the first element of the subarray in the main array.
     * @param last  Relative index of the last element of the subarray in the main array.
     * @return      If <code>ceil</code> is greater than <code>floor</code> then number of integers
     *              between them <strong>excluding</strong> themselves in the array is returned. If <code>ceil</code>
     *              is lesser than <code>floor</code> then negative of number of integers between them <strong>including</strong>
     *              themselves. If they are equal or not found in the array -1.
     */
    private static int countInBetween(int[] arr, int floor, int ceil, int first, int last) {
        // If index of either end elements of subarray is out of main array
        if(first < 0 || first >= arr.length || last < 0 || last >= arr.length){
            return -1;
        }

        // Change in indexes of subarray
        int change = Math.max((last - first) / 2, 1);

        // If first element is greater than floor
        if(arr[first] > floor){
            // Look for a valid subarray starting before this index
            return countInBetween(arr, floor, ceil, first - change, last);
        }
        // If first element is lesser than floor
        else if(arr[first] < floor){
            // Look for a valid subarray starting after this index
            return countInBetween(arr, floor, ceil, first + change, last);
        }
        // If first element is equal to floor
        else {
            // And there exist a next element
            if(first + 1 < arr.length){
                // And that element is also equal to floor
                if(arr[first + 1] == floor){
                    // Then check for next element, since floor is excluded
                    return countInBetween(arr, floor, ceil, first + 1, last);
                }
            }
            // No element was left for ceil
            else return -1;
        }

        // If last element is greater than ceil
        if(arr[last] > ceil){
            // Got to look for another one with last element before this
            return countInBetween(arr, floor, ceil, first, last - change);
        }
        // If last element is lesser than ceil
        else if(arr[last] < ceil){
            // Got to look for another one with last element after this
            return countInBetween(arr, floor, ceil, first, last + change);
        }
        // If last element is equal to ceil
        else{
            // And there exists another element before it
            if(last - 1 >= 0){
                // And it is also equal to ceil
                if(arr[last - 1] == ceil){
                    // Then check that element, since ceil is excluded
                    return countInBetween(arr, floor, ceil, first, last - 1);
                }
            }
            // No element was left for floor
            else return -1;
        }

        // Both floor and ceil was found successfully
        return last - first - 1;
    }

    /**
     * Finds continuous subarrays whose sum of its elements' is equal to given integer.
     * @param arr           Integer array.
     * @param sum           What shall continuous valid subarrays shall return upon adding up all of their elements.
     * @param i             Index of current element in the given array.
     * @param continuing    Is current element is part of a subarray whose first element is before it.
     * @return              List of integer lists whose sum is equal to given sum that are continuously found in given array.
     */
    private static List<List<Integer>> findSummingSubarrays(int[] arr, int sum, int i, boolean continuing) {
        // No element is left to check for, there's no valid subarray in this condition
        // hence the empty array
        if(i >= arr.length) return new ArrayList<>();

        // List of integer lists whose sum is equal to given sum that are continuously found in given array.
        List<List<Integer>> result = findSummingSubarrays(arr, sum - arr[i], i + 1, true); // get solution for if current element was part of the subarray

        // Current element itself is a solution, and it is also in
        // all of the solutions in lists in result up until now
        if(sum == arr[i]) result.add(new ArrayList<>());

        // so it is inserted to every one of them
        for(List<Integer> subArr : result){
            subArr.add(0, arr[i]);
        }

        // If current element is not part of a subarray that is already started to be checked
        if(!continuing){
            // Add all the solutions for the rest of the array too
            result.addAll(findSummingSubarrays(arr, sum, i + 1, false));
        }

        return result;
    }

    /**
     * Returns list of stringbuilders that represents 1D array fillings with blocks.
     * @param l Length of array.
     * @param i Index of current element in the array.
     * @param j Block length.
     * @param k 1 if in subarray 0 if not.
     * @return  List of stringbuilders that represents 1D array fillings with blocks.
     */
    private static List<StringBuilder> printArrayFillings(int l, int i, int j, int k) {

        // If length of array is less than or equal to block length
        if(l <= j){
            // If it is not part of a subarray and length of it is greater than two
            // then it can be filled with blocks, if not then it is empty, that's
            // why ' ? : ' is for
            return new ArrayList<>(Collections.singleton(new StringBuilder((k == 0 && l > 2 ? "#" : "-").repeat(Math.max(0, l)))));
        }

        // If block can't fit in the array
        if(i > l - j){
            // No solution
            return new ArrayList<>();
        }

        // Current filled part of the array
        String current = "#".repeat(j) + "-".repeat(l - i - j);

        // Get solutions for the part of the array before this index 'i'
        List<StringBuilder> result = printArrayFillings(i, 0, 3, 1);

        // List of invalid solutions to be removed from 'before'
        List<StringBuilder> remove = new ArrayList<>();

        for(StringBuilder stringBuilder : result){
            // These solutions are for earlier part of the array, and since
            // we want space in between blocks the last char of the solution
            // can't be full, if it's it will be removed
            if(stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '#'){
                remove.add(stringBuilder);
            }
            // If solution is valid, current part of the solution is added to it
            else{
                stringBuilder.append(current);
            }
        }

        // This solely for making print function prettier
        result.add(0, result.remove(result.size() - 1));

        // Remove invalid ones
        result.removeAll(remove);

        // Add the other solutions for rest of the array
        result.addAll(printArrayFillings(l, i + 1, j, k));

        // If all the possibilities of fillings with this block length
        // is found, hence block is at the end of the array, find the
        // other solutions for one longer block and add them
        if(i == l - j){
            result.addAll(printArrayFillings(l, 0, j + 1, k));
        }

        return result;
    }

    /**
     * Recursively fills the square matrix with snakes that can only bend laterally or vertically
     * and are long as side length of square matrix.
     * @param matrix    Matrix that is filled.
     * @param x         x coordinate of current position.
     * @param y         y coordinate of current position.
     * @param n         Number of remaining tiles to fill.
     * @return          <code>true</code> if matrix is filled. <code>false</code> otherwise.
     */
    private static boolean printMatrixFillings(int[][] matrix, int x, int y, int n) {
        // If position is outside of matrix it can't be filled
        if(x < 0 || x >= matrix.length || y < 0 || y >= matrix.length) return false;

        // If no tile remains to fill
        if(n < 0){
            // Print
            for (int[] ints : matrix) {
                for(int j = 0; j < matrix.length; ++j){
                    System.out.print(ints[j]);
                }
                System.out.println();
            }
            System.out.println();
            return true;
        }

        // If current position is available for filling
        if(matrix[x][y] == 0){
            // Fill it with unique integer to be able to tell apart the snakes
            // NOTE: the integers are only for visualization they can be inter
            // changeable it doesn't show any order or whatsoever
            matrix[x][y] = 1 + n / matrix.length;

            // Move through the matrix, this is done in or operators
            // to prevent trying to move through when matrix is full
            if(
                (
                    printMatrixFillings(matrix, x + 1, y, n - 1) ||
                    printMatrixFillings(matrix, x - 1, y, n - 1) ||
                    printMatrixFillings(matrix, x, y + 1, n - 1) ||
                    printMatrixFillings(matrix, x, y - 1, n - 1)
                ) && n < matrix.length // this is checked to prevent terminating all the process
            )
            {
                matrix[x][y] = 0;
                return true;
            }

            matrix[x][y] = 0;
        }

        return false;
    }
}

















