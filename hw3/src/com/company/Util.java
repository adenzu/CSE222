package com.company;

/**
 * Util class consists of static functions that
 * are used several times in the project and
 * don't really need or have a context or a relation
 * to any user-written class.
 */
public class Util {
    /**
     * Checks if given value is in range of given min and max values.
     * @param min   Minimum value that is allowed
     * @param max   Maximum value that is allowed
     * @param value Checked value
     * @return      <code>true</code> if value is at least <code>min</code>
     *              and at max <code>max</code>.
     */
    public static boolean isInRange(int min, int max, int value) {
        return min <= value && max >= value;
    }

    /**
     * Prints new line.
     */
    public static void print() {
        System.out.println();
    }

    /**
     * Prints given object.
     * @param object    Object to be printed.
     */
    public static void print(Object object) {
        System.out.println(object);
    }

    /**
     * Separates given string from the characters that
     * match the given character. And returns resulting
     * strings in an array.
     * @param string    String to be separated.
     * @param separator The character where the given
     *                  string is seperated at.
     * @return  Array of strings that resulted from
     *          separation.
     */
    public static String[] separateString(String string, char separator) {
        // Number of strings resulting from separation
        // at least one since a string is given
        int partCount = 1;

        // Count the number of times the separator
        // character is found
        for(int i = 0; i < string.length(); ++i){
            if(string.charAt(i) == separator){
                ++partCount;
            }
        }

        String[] result = new String[partCount];

        for(int i = 0, j = 0; i < partCount; ++i){
            result[i] = ""; // Initialize

            // Add each character from the original string
            // to current string until separator char is
            // reached
            while(j < string.length() && string.charAt(j) != separator){
                result[i] += string.charAt(j);
                ++j;
            }

            ++j;
        }

        return result;
    }
}
