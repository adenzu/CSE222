package com.company;

/**
 * Class that contains few different array sorting methods.
 */
public class SortingMethods {
    /**
     * Merge sorts given array.
     * @param arr   Array to be sorted.
     * @return      Array after it's sorted.
     * @param <E>   Class of array which must extend Comparable.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Comparable<E>> E[] MergeSort(E[] arr) {
        if(arr.length < 2) return arr;

        int mid = arr.length / 2;

        E[] left  = (E[]) new Comparable[mid];
        E[] right = (E[]) new Comparable[arr.length - mid];

        System.arraycopy(arr, 0, left, 0, left.length);
        System.arraycopy(arr, mid, right, 0, right.length);

        MergeSort(left);
        MergeSort(right);

        int i = 0, j = 0;
        while(i + j < arr.length){
            if(j >= right.length || (i < left.length && left[i].compareTo(right[j]) < 0)){
                arr[i + j] = left[i];
                i++;
            }
            else{
                arr[i + j] = right[j];
                j++;
            }
        }

        return arr;
    }

    /**
     * Quick sorts given array.
     * @param arr   Array to be sorted.
     * @return      Array after it's sorted.
     * @param <E>   Class of the array, which must extend Comparable.
     */
    public static <E extends Comparable<E>> E[] QuickSort(E[] arr) {
        return QuickSort(arr, 0, arr.length - 1);
    }

    /**
     * New sorts given array.
     * @param arr   Array to be sorted.
     * @return      Array after it's sorted.
     * @param <E>   Class of the array, which must extend Comparable.
     */
    public static <E extends Comparable<E>> E[] NewSort(E[] arr) {
        return NewSort(arr, 0, arr.length - 1);
    }

    /**
     * Well known quicksort algorithm.
     * @param arr   Array
     * @param head  First element's index
     * @param tail  Last element's index
     * @return      Sorted array
     * @param <E>   Array class
     */
    private static <E extends Comparable<E>> E[] QuickSort(E[] arr, int head, int tail) {
        if(tail - head < 1) return null;

        int pivot = partition(arr, head, tail);

        QuickSort(arr, head, pivot - 1);
        QuickSort(arr, pivot + 1, tail);

        return arr;
    }

    /**
     * Selects the first element as pivot and partitions the array according to it.
     * @param arr   Array
     * @param head  First element's index
     * @param tail  Last element's index
     * @return      Partitioned array
     * @param <E>   Array class
     */
    private static <E extends Comparable<E>> int partition(E[] arr, int head, int tail) {
        int pivotIndex = head;
        E pivot = arr[head++];

        do {
            while(head < tail && arr[head].compareTo(pivot) <= 0) head++;
            while(arr[tail].compareTo(pivot) > 0) tail--;

            if(head < tail){
                E temp = arr[head];
                arr[head] = arr[tail];
                arr[tail] = temp;
            }
        } while(head < tail);

        arr[pivotIndex] = arr[tail];
        arr[tail] = pivot;

        return tail;
    }

    /**
     * Sorts given array by finding the minimum and maximum elements in given range in given array and putting them to
     * ends of the array.
     * @param arr   Array
     * @param head  First element's index
     * @param tail  Last element's index
     * @return      Sorted array
     * @param <E>   Array class
     */
    private static <E extends Comparable<E>> E[] NewSort(E[] arr, int head, int tail) {
        if(head > tail) return arr;
        else{
            int min = head, max = tail;

            for(int i = head; i < tail; ++i) {
                if (arr[min].compareTo(arr[i]) > 0) {
                    min = i;
                }

                if (arr[max].compareTo(arr[i]) < 0) {
                    max = i;
                }
            }

            E temp = arr[head];
            arr[head] = arr[min];
            arr[min] = temp;

            temp = arr[tail];
            arr[tail] = arr[max];
            arr[max] = temp;

            return NewSort(arr,head + 1, tail - 1);
        }
    }
}
