package com.company;

import com.company.Street.Side;

import javax.management.InvalidAttributeValueException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class.
 */
public class Main {

    /**
     * Scanner for getting input from the user through the terminal.
     */
    static Scanner scanner = new Scanner(System.in);

    /**
     * The street of the program, this is where all architectures are built on.
     */
    static Street street = null;

    /**
     * Max height for the street silhouette to be printed.
     */
    static int silhouetteHeight = 5;

    /**
     * Main function.
     * @param args  Console arguments.
     */
    public static void main(String[] args) {
        driverFunc1();
        driverFunc();
        programMenu();
    }

    /**
     * Measures time of addition and removal of architectures on given street.
     * @param street            Street for architectures to be added to
     * @param architectureCount Number of architectures in each test run to be added and
     *                          removed
     * @param testCount         Number of addition and removal of <code>architectureCount</code> many architectures
     */
    public static void streetArchitectureAdditionTimer(Street street, long architectureCount, long testCount) {
        try{
            // Time measurements
            long additionTime = 0;
            long removalTime = 0;

            for(int i = 0; i < testCount; ++i){

                // Start of addition
                long st = System.currentTimeMillis();

                // Add playgrounds for testing street speed since playground construction is simplest
                for(int j = 0; j < architectureCount; ++j){
                    Architecture architecture = new Playground(10 * j, 5);
                    street.addArchitecture(architecture, Side.Right);
                }

                // Add to addition time
                additionTime += System.currentTimeMillis() - st;

                // Start of removal
                st = System.currentTimeMillis();

                // Clear out the street
                while(street.getArchitectureCount(Side.Right) > 0){
                    street.removeArchitecture(0, Side.Right);
                }

                // Add to removal time
                removalTime += System.currentTimeMillis() - st;
            }

            // Print results
            float avgAdd = (float) additionTime / testCount;
            float avgRem = (float) removalTime / testCount;
            float avgTot = avgAdd + avgRem;
            Util.print("Took " + avgAdd + " ms on average for " + testCount + " runs of " + architectureCount + " architecture addition.");
            Util.print("Took " + avgRem + " ms on average for " + testCount + " runs of " + architectureCount + " architecture removal.");
            Util.print("Took average of " + avgTot + " ms in total.");
        } catch (Exception e){
            Util.print(e);
        }
    }

    /**
     * Showcases execution times of different street types
     */
    public static void driverFunc1() {
        // Self-explanatory variables
        int testCount = 100;
        int architectureCount = 1_000;
        int streetLength = 1_000_000;

        try {
            // Showcasing execution times of different types of streets with two different
            // architecture count
            for(int i = 0; i < 2; ++i){
                Util.print("Initiating time measurement of addition and removal of architectures on various types of street...");
                Util.print("#test = " + testCount + ", #architecture = " + architectureCount);
                Util.print();

                Util.print("Original implementation of street:");
                streetArchitectureAdditionTimer(new OldStreet(streetLength), architectureCount, testCount);
                Util.print();

                Util.print("Street implemented with ArrayList:");
                streetArchitectureAdditionTimer(new ArrayListStreet(streetLength), architectureCount, testCount);
                Util.print();

                Util.print("Street implemented with LinkedList:");
                streetArchitectureAdditionTimer(new LinkedListStreet(streetLength), architectureCount, testCount);
                Util.print();

                Util.print("Street implemented with LDLinkedList:");
                streetArchitectureAdditionTimer(new LDLinkedListStreet(streetLength), architectureCount, testCount);
                Util.print();

                architectureCount *= 2;
            }
        } catch (Exception e) {
            Util.print(e);
        }

        Util.print("Press enter to continue...");
        scanner.nextLine();
    }

    /**
     * Showcases all the functionalities of the program.
     */
    public static void driverFunc() {
        Util.print();
        Util.print("Initiating former driver function...");
        Util.print();

        try{
            Util.print("1) Street creation");
            Util.print();

            Util.print("Trying to create a street with length of -10:");
            new OldStreet(-10);
        }
        catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("Streets can't have negative values as their length");
        }

        try {
            Util.print("Creating a street with length of 0");
            new OldStreet(0);
            Util.print("Creation successful");
            Util.print();

            Util.print("Street length: " + street.getLength());
            Util.print("Streets are allowed to be 0 length long");
        } catch (Exception ignored) { }


        try{
            Util.print("Creating a street with length of 999999");
            AbstractStreet temp = new OldStreet(999999);
            Util.print();

            Util.print("Creation successful");
            Util.print("Street length: " + temp.getLength());
            Util.print("Streets don't have a maximum limit for their length");
        }
        catch (Exception ignored) { }
        Util.print();
        Util.print();

        Util.print("2) Proceeding to architecture creation");
        Util.print();

        Util.print("Architectures can't be created with negative");
        Util.print("values as their length and height");
        Util.print();

        try{
            Util.print("Trying to create a house with -10 length");
            new House(0, -10, 0, new Owner(""), 1, new Color(0,0,0));
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("No architecture can have negative length");
        }
        Util.print();

        try{
            Util.print("Trying to create a house with -10 height");
            new House(0, 0, -10, new Owner(""), 1, new Color(0,0,0));
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("No architecture can have negative height");
        }
        Util.print();

        try{
            Util.print("Trying to create a house at position -10");
            new House(-10, 0, 0, new Owner(""), 1, new Color(0,0,0));
            Util.print("Creation successful");
            Util.print();

            Util.print("This is allowed, if a street were to start at the position -10");
            Util.print("then this house could be built on such street, but in this program");
            Util.print("every street starts from 0 position, therefore any architecture with");
            Util.print("negative value as their position won't be added to streets and will");
            Util.print("cause an exception, which will be handled by the program");
        } catch (InvalidAttributeValueException ignored) { }
        Util.print();

        Util.print("Houses are buildings and every building has to have an owner");
        Util.print("Owner objects only have a name field that is string, there's no");
        Util.print("restriction for owner names");
        Util.print();

        Util.print("Creating an owner with '' as its name");
        new Owner("");
        Util.print("Creation successful");
        Util.print();

        try{
            Util.print("Trying to create a house with -2 rooms");
            new House(0, 0, 0, new Owner(""), -2, new Color(0,0,0));
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("This is not possible a house at least has to have 1 room");
        }
        Util.print();

        try{
            Util.print("Trying to create a house with 0 rooms");
            new House(0, 0, 0, new Owner(""), 0, new Color(0,0,0));
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("This is not possible a house at least has to have 1 room");
        }
        Util.print();

        try{
            Util.print("Trying to create a house with 5 rooms");
            new House(0, 0, 0, new Owner(""), 5, new Color(0,0,0));
            Util.print("Creation successful");
            Util.print("Houses don't have an upper limit for their number of rooms");
        } catch (InvalidAttributeValueException ignored) { }
        Util.print();

        try{
            Util.print("Houses have color, colors have red, green, blue, and alpha values");
            Util.print("ranging from 0 to 255 included, colors with values not in this range");
            Util.print("simply can't be");
            Util.print();

            Util.print("Trying to create a color with -25 redness value");
            new Color(-25, 0, 0);
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("Other values of the color class also behave this way");
        }
        Util.print();

        try{
            Util.print("Trying to create a color with 300 green value");
            new Color(0, 300, 0);
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("Other values of the color class also behave this way");
        }
        Util.print();

        try{
            Util.print("Trying to create a color with with values");
            Util.print("16, 32, 64, 128, as its red, green, blue, and alpha");
            Util.print("attributes in order");
            new Color(16, 32, 64, 128);
            Util.print("Creation successful");
        } catch (InvalidAttributeValueException ignored) { }
        Util.print();

        try{
            Util.print("Markets, one of the other type of buildings, have opening");
            Util.print("and closing times unique to it that other building don't have");
            Util.print("Time class has attributes hours, minutes, and seconds with valid");
            Util.print("integer ranges of 0-23, 0-59, 0-59 in that order");
            Util.print();

            Util.print("Trying to create a time with -5 as its hours attribute");
            new Time(-5, 0, 0);
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("Other parameters of time also can't be negative");
        }
        Util.print();

        try{
            Util.print("Trying to create a time with 24 as its hours attribute");
            new Time(24, 0, 0);
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("It can only be 23 at max");
        }
        Util.print();

        try{
            Util.print("Trying to create a time with 70 as its minutes attribute");
            new Time(0, 70, 0);
        } catch (InvalidAttributeValueException e) {
            Util.print(e.getMessage());
            Util.print("Seconds attribute also can't be greater than 59");
        }
        Util.print();

        try {
            Util.print("Offices have job types different from other buildings");
            Util.print("job types are kept in a String array and there is");
            Util.print("no restrictions included by the programmer (me)");
            Util.print("for the strings");
            Util.print();

            Util.print("Creating an office with job types:");
            Util.print("'', 'asldjodşalsmdş', 'AMAZON INC'");
            new Office(0, 0, 0, new Owner(""), new String[]{"", "asldjodşalsmdş", "AMAZON INC"});
            Util.print("Creation successful");
        } catch (InvalidAttributeValueException ignored) { }

        Util.print("Playgrounds are not buildings bur only architecture");
        Util.print("they only have position and length attributes which");
        Util.print("are tested already, moving on");
        Util.print();
        Util.print();

        Util.print("3) Street and architecture interaction");
        Util.print();

        Util.print("Architectures can't be outside the street");
        Util.print("either partially or altogether, there also can't");
        Util.print("be more than one architecture occupying same space");
        Util.print();

        Util.print("Creating a street with length 1000 for test");
        try {
            street = new OldStreet(1000);
            Util.print("Creation successful");
        } catch (InvalidAttributeValueException ignored) { }
        Util.print();

        Util.print("For testing exceptions mentioned above playgrounds will be");
        Util.print("used as they can cause all of those and have less parameters for me to write");
        Util.print();

        try {
            Util.print("Adding a playground at -5 position on left side of the street");
            street.addArchitecture(new Playground(-5, 0), Side.Left);
        } catch (Exception e) {
            Util.print(e.getMessage());
        }
        Util.print();

        try {
            Util.print("Adding a playground at 990 position on left side of the street");
            Util.print("with length of 20, with resulting end point at 1010 (street length is 1000 unit)");
            street.addArchitecture(new Playground(990, 20), Side.Left);
        } catch (Exception e) {
            Util.print(e.getMessage());
        }
        Util.print();

        try {
            Util.print("Adding a playground at 0 position on left side of the street");
            Util.print("with length of 20");
            street.addArchitecture(new Playground(0, 20), Side.Left);
            Util.print("Creation successful");
            Util.print();

            Util.print("Adding a playground at 0 position on right side of the street");
            Util.print("with length of 20");
            street.addArchitecture(new Playground(0, 20), Side.Right);
            Util.print("Creation successful");
            Util.print();

            Util.print("Since playgrounds are on different sides this is allowed");
            Util.print();

            Util.print("Adding a playground at 10 position on *left* side of the street");
            Util.print("with length of 20, this will result in an exception since there is");
            Util.print("a playground there");
            street.addArchitecture(new Playground(10, 20), Side.Left);
        } catch (Exception e) {
            Util.print(e.getMessage());
        }

        try {
            Util.print("To remove an architecture from a street one has to pass same instance");
            Util.print("or a valid index to remove function.");
            Util.print();

            Util.print("Trying to remove a playground at position 0 with length of 20 on left");
            Util.print("side of the street but this is a different instance than the");
            Util.print("previously added one with same values, therefore removal will result in an exception");
            street.removeArchitecture(new Playground(0, 20));
        } catch (Exception e) {
            Util.print(e.getMessage());
        }

        try{
            Util.print("One also can remove an architecture at an index");
            Util.print("architectures are added at the end of the array");
            Util.print("currently there are two playgrounds on each side");
            Util.print("so valid indexes are 0 on left and 0 on right");
            Util.print();

            Util.print("Trying to remove at index 1 on left");
            street.removeArchitecture(1, Side.Left);
        } catch (IndexOutOfBoundsException e){
            Util.print(e.getMessage());
            Util.print("index was greater than architecture count");
        }
        Util.print();

        try{
            Util.print("Trying to remove at index -1 on right");
            street.removeArchitecture(-1, Side.Right);
        } catch (IndexOutOfBoundsException e){
            Util.print(e.getMessage());
            Util.print("index was lesser than 0");
        }
        Util.print();
        try{
            Util.print("Trying to remove at index 0 on right");
            street.removeArchitecture(0, Side.Right);
            Util.print("Removal successful");
        } catch (IndexOutOfBoundsException ignored){ }
        Util.print();

        Util.print("Trying to get an architecture at an index also have the same");
        Util.print("exceptions for the same conditions");
        Util.print();
        Util.print();

        Util.print("4) Viewing mode functionalities");
        Util.print();

        Util.print("A street with length 50 and several architectures");
        Util.print("will be created an added to that street to test viewing");
        Util.print("mode");
        Util.print();

        try {
            street = new OldStreet(50);
        } catch (InvalidAttributeValueException ignored) { }

        Owner me = new Owner("Eren Çakar");
        Owner mehmet = new Owner("Mehmet Usta");
        Owner richah = new Owner("Elon Musk");

        House myHouse, mehmetHouse;
        Market mehmetMarket;
        Office ahOffice;
        Playground playground1, playground2;

        try {
            myHouse = new House(10, 15, 7, me, 5, new Color(255, 0, 0));
            Util.print("Added " + myHouse + " on left side of the street");
            street.addArchitecture(myHouse, Side.Left);
        } catch (Exception ignored) { }
        Util.print();

        try {
            mehmetHouse = new House(2, 10, 5, mehmet, 3, new Color(0, 255, 0));
            Util.print("Added " + mehmetHouse + " on right side of the street");
            street.addArchitecture(mehmetHouse, Side.Right);
        } catch (Exception ignored) { }
        Util.print();

        try {
            mehmetMarket = new Market(20, 7, 10, mehmet, new Time(8, 30, 0), new Time(21, 0, 0));
            Util.print("Added " + mehmetMarket + " on right side of the street");
            street.addArchitecture(mehmetMarket, Side.Right);
        } catch (Exception ignored) { }
        Util.print();

        try {
            ahOffice = new Office(0, 8, 25, richah, new String[]{"Employer", "Manager", "Driver", "Packager"});
            Util.print("Added " + ahOffice + " on left side of the street");
            street.addArchitecture(ahOffice, Side.Left);
        } catch (Exception ignored) { }
        Util.print();

        try {
            playground1 = new Playground(35, 5);
            Util.print("Added " + playground1 + " on left side of the street");
            street.addArchitecture(playground1, Side.Left);
        } catch (Exception ignored) { }
        Util.print();

        try {
            playground2 = new Playground(41, 7);
            Util.print("Added " + playground2 + " on right side of the street");
            street.addArchitecture(playground2, Side.Right);
        } catch (Exception ignored) { }
        Util.print();

        Util.print("Displaying total remaining length:");
        printTotalRemainingLengths();
        Util.print();

        Util.print("Displaying architecture list:");
        printListOfArchitectures();
        Util.print();

        Util.print("Displaying playground occupation info:");
        printPlaygroundOccupationInfo();
        Util.print();

        Util.print("Displaying building occupation info:");
        printBuildingOccupationInfo();
        Util.print();

        Util.print("Displaying street silhouette:");
        printStreetSilhouette(30);
        Util.print();

        Util.print("Terminating functionality testing sequence...");
        Util.print();
        Util.print();
        Util.print();
        Util.print();
    }

    /**
     * Starts the program interface.
     */
    public static void programMenu() {
        Util.print("Welcome to the street planner!");
        Util.print("To select menu options enter the text before the ')' for each option.\n");

        streetCreation();
        modeMenu();
    }

    /**
     * Prints 'Invalid input, try again.'. Used for informing
     * the user that input is ill-formed.
     */
    public static void printInvalidInput() {
        Util.print("Invalid input, try again.");
    }

    /**
     * Reads an <code>int</code> value from the terminal and returns it.
     * Doesn't stop until reads an integer.
     * @return <code>int</code> value that is read
     */
    public static int readInt() {
        int result;
        while (true){
            // Try reading an integer
            try{
                result = scanner.nextInt();
                break;
            }
            // Upon reading non-integer inform the user
            catch(InputMismatchException e){
                printInvalidInput();
                scanner.next();     // Flushes newline character
            }
        }
        return result;
    }

    /**
     * Reads an <code>int</code> from the terminal in
     * the given range. Doesn't stop until a valid
     * input is entered.
     * @param min   Minimum <code>int</code> value to be entered, included
     * @param max   Maximum <code>int</code> value to be entered, included
     * @return  Entered value
     */
    public static int readIntInRange(int min, int max) {
        int result;
        while(true){
            result = readInt();

            if(result < min || result > max){
                Util.print("Enter a value between " + min + "-" + max + " included.");
            }
            else{
                return result;
            }
        }
    }

    /**
     * Reads an <code>int</code> from the terminal that
     * is greater than given value. Doesn't stop until a
     * valid input is entered.
     * @param than  Minimum <code>int</code> value to be entered, excluded
     * @return  Entered value
     */
    public static int readIntGreater(int than) {
        int result;
        while(true){
            result = readInt();

            if(result <= than){
                Util.print("Enter a value greater than " + than + " included.");
            }
            else{
                return result;
            }
        }
    }

    /**
     * Reads an <code>int</code> from the terminal that
     * is lesser than given value. Doesn't stop until a
     * valid input is entered.
     * @param than  Maximum <code>int</code> value to be entered, excluded
     * @return  Entered value
     */
    public static int readIntLesser(int than) {
        int result;
        while(true){
            result = readInt();

            if(result >= than){
                Util.print("Enter a value lesser than " + than + " included.");
            }
            else{
                return result;
            }
        }
    }

    /**
     * Reads an entire line from the terminal.
     * @return  The entire line that is read
     */
    public static String readStringLine() {
        String result = "\n";
        // Sometimes newline characters remain on the console
        // from the previous read, this while loop prevents
        // blank string reads causing from that
        while (result.equals("\n") || result.length() == 0) result = scanner.nextLine();
        return result;
    }

    /**
     * Reads an <code>int</code> value and returns corresponding <code>Side</code> value.
     * Demands either 1 or 2 to be entered, to return <code>Side.Left</code> or
     * <code>Side.Right</code> in order.
     * @return Corresponding <code>Side</code> value to the read <code>int</code>
     */
    public static Side readSide() {
        Side result;

        Util.print("Enter side of the street to build on");
        Util.print("1) Left");
        Util.print("2) Right");
        while (true){
            // Try returning the corresponding Side value
            try{
                result = Side.sides[readInt() - 1];
                break;
            }
            // If read int is out ouf bounds then inform the user
            catch(IndexOutOfBoundsException e){
                printInvalidInput();
            }
        }
        return result;
    }

    /**
     * Reads terminal input in the form of 'HOURS:MINUTES' or 'HOURS:MINUTES:SECONDS'
     * and returns <code>Time</code> value with given values.
     * @return <code>Time</code> value with read values
     */
    public static Time readTime() {
        // The read string is a one whole string, but
        // it needs to be separated to be parsed to
        // integer, this is where separated strings
        // are held
        String[] timeS;
        while(true){
            // Read and separate
            timeS = Util.separateString(readStringLine(), ':');

            try{
                // Initialization with two parameters
                if(timeS.length == 2){
                    return new Time(Integer.parseInt(timeS[0]), Integer.parseInt(timeS[1]), 0);
                }
                // Initialization with three parameters
                else if(timeS.length == 3){
                    return new Time(Integer.parseInt(timeS[0]), Integer.parseInt(timeS[1]), Integer.parseInt(timeS[2]));
                }
                // Other cases are ill formed, user is informed
                else{
                    Util.print("Enter as HOURS:MINUTES or HOURS:MINUTES:SECONDS");
                }
            }
            // If values for hour or minute or second are invalid
            // inform the user
            catch (InvalidAttributeValueException e){
                Util.print(e.getMessage());
            }
            // If strings couldn't be parsed then inform the user
            catch (Exception e) {
                printInvalidInput();
            }
        }
    }

    /**
     * The street creation menu. User is wanted to
     * enter a positive <code>int</code> as a length
     * value for the street that is going to be
     * worked on.
     */
    public static void streetCreation() {
        Util.print("Enter an integer value for the length of the street:");

        try{
            street = new OldStreet(readIntGreater(-1));
        }
        catch (Exception ignored) { }   // impossible
    }

    /**
     * The base menu of the program. User either
     * starts the editing mode or the viewing mode
     * here, user can also terminate the program.
     */
    public static void modeMenu() {
        // No further explanation is needed, self-explanatory
        while(true){
            Util.print("1) Start editing mode");
            Util.print("2) Start viewing mode");
            Util.print("0) Exit");

            switch(readInt()){
                case 1:
                    editingMode();
                    break;
                case 2:
                    viewingMode();
                    break;
                case 0:
                    return;
                default:
                    printInvalidInput();
            }
        }
    }

    /**
     * The editing mode menu. User can either add or
     * remove any type of building or playground by
     * selecting the option here.
     */
    public static void editingMode() {
        // No further explanation is needed, self-explanatory
        while(true){
            Util.print("1) Add an architecture");
            Util.print("2) Remove an architecture");
            Util.print("0) Back");

            switch(readInt()){
                case 1:
                    addArchitectureMenu();
                    break;
                case 2:
                    removeArchitectureMenu();
                    break;
                case 0:
                    return;
                default:
                    printInvalidInput();
            }
        }
    }

    /**
     * The viewing mode menu. User can display various
     * information about the street here.
     */
    public static void viewingMode() {
        // No further explanation is needed, self-explanatory
        while(true){
            Util.print("1) Display total remaining length");
            Util.print("2) Display list of buildings");
            Util.print("3) Display total length occupied by playgrounds and its ratio to total length");
            Util.print("4) Display total length occupied by markets, houses, and offices");
            Util.print("5) Display street silhouette");
            Util.print("0) Back");

            switch(readInt()){
                case 1:
                    printTotalRemainingLengths();
                    break;
                case 2:
                    focusMenu();
                    break;
                case 3:
                    printPlaygroundOccupationInfo();
                    break;
                case 4:
                    printBuildingOccupationInfo();
                    break;
                case 5:
                    printStreetSilhouette(silhouetteHeight);
                    break;
                case 0:
                    return;
                default:
                    printInvalidInput();
            }
        }
    }

    /**
     * This is basically the menu where the user
     * can display the list of all the architectures
     * that are built on the street. User also can
     * focus on any instance of architecture.
     */
    public static void focusMenu() {
        while (true){
            // Display list of architectures
            Util.print("Enter the index of the architecture to focus on:");
            printListOfArchitectures();
            Util.print("0) Back");

            // Indexes of the architectures in the list
            // starts from 1 but in array 0 so 1 is deducted
            int input = readInt() - 1;

            if(input == -1) return;
            else{
                Side side; // Side of the street that the architecture is on
                int index; // Index of the architecture on that side

                // Listing starts with the architectures on the left
                // therefore this inequality to check if the entered
                // option index corresponds to an architecture on
                // the left side of the street
                if(input < street.getArchitectureCount(Side.Left)){
                    side = Side.Left;
                    index = input;
                }
                // If input is greater than the architecture has to
                // be on the right side
                else{
                    side = Side.Right;

                    // Architecture indexes start from 0 on each side
                    // of the sides, so architecture count on the left
                    // side is deducted from the input
                    index = input - street.getArchitectureCount(Side.Left);
                }

                // Get the architecture and print the focus result
                try{
                    Util.print("Focused on the " + input + "th architecture:\n" + street.getArchitecture(index, side).focus());
                }
                // If entered index is out of range, inform the user
                catch (IndexOutOfBoundsException e) {
                    printInvalidInput();
                }
            }
        }
    }

    /**
     * Architecture creation menu of the program. This
     * is where the user creates/adds new architectures
     * of their liking on the street.
     */
    public static void addArchitectureMenu() {
        while(true){
            Util.print("Add:");
            Util.print("1) House");
            Util.print("2) Market");
            Util.print("3) Office");
            Util.print("4) Playground");
            Util.print("0) Back");

            // When user wants to display the silhouette
            // of the street there has to be a value for
            // maximum height for it, this is achieved
            // by comparing every and any new architecture's
            // height to existing value and updating if
            // height of architecture exceeds
            int addedArchHeight = 0;

            switch(readInt()){
                case 1:
                    addedArchHeight = addHouse();
                    break;
                case 2:
                    addedArchHeight = addMarket();
                    break;
                case 3:
                    addedArchHeight = addOffice();
                    break;
                case 4:
                    addedArchHeight = addPlayground();
                    break;
                case 0:
                    return;
                default:
                    printInvalidInput();
            }

            // Update silhouette height
            if(addedArchHeight > silhouetteHeight){
                silhouetteHeight = addedArchHeight + 1;
            }
        }
    }

    /**
     * Architecture removal menu of the program. User
     * removes any existing architecture on the street
     * of their liking here.
     */
    public static void removeArchitectureMenu() {
        while (true){

            // Display list of architectures
            Util.print("Enter the index of the architecture to remove it:");
            printListOfArchitectures();
            Util.print("0) Back");

            // Indexes of the architectures in the list
            // starts from 1 but in array 0 so 1 is deducted
            int input = readInt() - 1;

            if(input == -1) return;
            else{
                Side side; // Side of the street that the architecture is on
                int index; // Index of the architecture on that side

                // Listing starts with the architectures on the left
                // therefore this inequality to check if the entered
                // option index corresponds to an architecture on
                // the left side of the street
                if(input < street.getArchitectureCount(Side.Left)){
                    side = Side.Left;
                    index = input;
                }
                // If input is greater than the architecture has to
                // be on the right side
                else{
                    side = Side.Right;

                    // Architecture indexes start from 0 on each side
                    // of the sides, so architecture count on the left
                    // side is deducted from the input
                    index = input - street.getArchitectureCount(Side.Left);
                }

                // Remove the architecture and print successful remove
                try{
                    street.removeArchitecture(index, side);
                    Util.print("Removed!");
                }
                // If entered index is out of range, inform the user
                catch (IndexOutOfBoundsException e) {
                    printInvalidInput();
                }
            }
        }
    }

    /**
     * Adds given architecture to street of the program. User is
     * needed to enter a side of the street for architecture to
     * be added on.
     * @param architecture  Architecture to be built/added to the street
     * @throws AbstractStreet.InvalidPositionException  When position of the architecture is outside
     *                                          the street
     * @throws AbstractStreet.PositionOccupiedException When another architecture is in the way
     * @throws AbstractStreet.NotEnoughSpaceException   When architecture doesn't fit in the street
     */
    public static void addArchitecture(Architecture architecture) throws AbstractStreet.InvalidPositionException, AbstractStreet.PositionOccupiedException, AbstractStreet.NotEnoughSpaceException {
        Side side = readSide();
        street.addArchitecture(architecture, side);
    }

    /**
     * This is the menu of the house addition/creation.
     * User has to complete the creation of a house,
     * until so there's no exit.
     * @return  Height of the created house in <code>int</code>
     */
    public static int addHouse() {
        // Parameters needed for house creation
        int position, length, height;
        Owner owner;
        int roomCount;
        Color color = null;

        // Created house
        House newHouse;

        // Get number of rooms of the house
        Util.print("Enter number of rooms of the house:");
        roomCount = readIntGreater(-1);

        // Get the color of the house
        while(color == null){
            try{
                Util.print("Enter red, green, and blue values (ranging 0-255) of the color of the house:");
                color = new Color(readIntInRange(0, 255), readIntInRange(0, 255), readIntInRange(0, 255));
            }
            catch (InvalidAttributeValueException e) {
                printInvalidInput();
            }
        }

        // Get the owner of the house
        Util.print("Enter the name of the owner of the house:");
        owner = new Owner(readStringLine());

        while(true){
            while(true){
                Util.print("Enter position:");
                position = readIntInRange(0, street.getLength());

                Util.print("Enter length:");
                length = readIntGreater(-1);

                Util.print("Enter height:");
                height = readIntGreater(-1);

                // Try constructing the house
                try {
                    newHouse = new House(position, length, height, owner, roomCount, color);
                    break;
                }
                // If any parameter is invalid inform the user
                catch (InvalidAttributeValueException e){
                    Util.print(e.getMessage());
                }
            }

            // Try adding it to street
            try{
                addArchitecture(newHouse);
                Util.print("Added the new house!");
                break;
            }
            // If any of the street adding related exceptions occur
            // inform the user
            catch(Exception e) {
                Util.print(e.getMessage());
            }
        }

        return height;
    }

    /**
     * This is the menu of the market addition/creation.
     * User has to complete the creation of a market,
     * until so there's no exit.
     * @return  Height of the created market in <code>int</code>
     */
    public static int addMarket() {
        // Parameters for market creation
        int position, length, height;
        Owner owner;
        Time openTime, closeTime;

        // New market
        Market newMarket;

        Util.print("Enter the name of the owner of the market:");
        owner = new Owner(readStringLine());

        Util.print("Enter opening time: (separate by ':')");
        openTime = readTime();

        Util.print("Enter closing time: (separate by ':')");
        closeTime = readTime();

        while(true){
            while(true){
                Util.print("Enter position:");
                position = readIntInRange(0, street.getLength());

                Util.print("Enter length:");
                length = readIntGreater(-1);

                Util.print("Enter height:");
                height = readIntGreater(-1);

                try {
                    newMarket = new Market(position, length, height, owner, openTime, closeTime);
                    break;
                }
                catch (InvalidAttributeValueException e){
                    Util.print(e.getMessage());
                }
            }

            // Try adding it to street
            try{
                addArchitecture(newMarket);
                Util.print("Added the new market!");
                break;
            }
            // If any of the street adding related exceptions occur
            // inform the user
            catch(Exception e) {
                Util.print(e.getMessage());
            }
        }

        return height;
    }

    /**
     * This is the menu of the office addition/creation.
     * User has to complete the creation of an office,
     * until so there's no exit.
     * @return  Height of the created office in <code>int</code>
     */
    public static int addOffice() {
        // Parameters for office creation
        int position, length, height;
        Owner owner;
        StringBuilder jobTypes = new StringBuilder();

        // New office
        Office newOffice;

        Util.print("Enter the name of the owner of the office:");
        owner = new Owner(readStringLine());

        Util.print("Enter job types: (enter 0 to stop)");
        String input;
        while(true){
            input = readStringLine();
            if(input.equals("0") && jobTypes.length() > 0) break;
            jobTypes.append(input).append(input.length() > 0 ? '|' : "");
        }

        while(true){
            while(true){
                Util.print("Enter position:");
                position = readIntInRange(0, street.getLength());

                Util.print("Enter length:");
                length = readIntGreater(-1);

                Util.print("Enter height:");
                height = readIntGreater(-1);

                try {
                    newOffice = new Office(position, length, height, owner, Util.separateString(jobTypes.toString(), '|'));
                    break;
                }
                catch (InvalidAttributeValueException e){
                    Util.print(e.getMessage());
                }
            }

            // Try adding to street
            try{
                addArchitecture(newOffice);
                Util.print("Added the new office!");
                break;
            }
            // If any of the street adding related exceptions occur
            // inform the user
            catch(Exception e) {
                Util.print(e.getMessage());
            }
        }

        return height;
    }

    /**
     * This is the menu of the playground addition/creation.
     * User has to complete the creation of a playground,
     * until so there's no exit.
     * @return  Height of the created playground in <code>int</code>
     */
    public static int addPlayground() {
        // Parameters for playground creation
        int position, length;

        // New playground
        Playground newPlayground;

        while(true){
            while(true){
                Util.print("Enter position:");
                position = readIntGreater(-1);

                Util.print("Enter length:");
                length = readIntGreater(-1);


                try {
                    newPlayground = new Playground(position, length);
                    break;
                }
                catch (InvalidAttributeValueException e){
                    Util.print(e.getMessage());
                }
            }

            try{
                addArchitecture(newPlayground);
                Util.print("Added the new playground!");
                break;
            }
            // If any of the street adding related exceptions occur
            // inform the user
            catch(Exception e) {
                Util.print(e.getMessage());
            }
        }

        return newPlayground.getHeight();
    }

    /**
     * Prints the remaining lengths on each side
     * of the street.
     */
    public static void printTotalRemainingLengths() {
        for(Side side : Side.sides) {
            Util.print("Total of " + street.getRemainingLength(side) + " length remains on the " + getSideName(side) + " side of the street.");
        }
    }

    /**
     * Prints list of all architectures on both
     * sides of the street with indexes.
     */
    public static void printListOfArchitectures() {
        // Index of the architecture
        int index = 0;

        // For each side
        for(Side side : Side.sides) {
            Util.print("On the " + getSideName(side) + " side of the street there is:");

            // If there's any architecture on
            if(street.getArchitectureCount(side) > 0){
                // Print them
                for(int i = 0; i < street.getArchitectureCount(side); ++i) {
                    Util.print("\t" + (++index + ") ") + street.getArchitecture(i, side));
                }
            }
            // If not inform so
            else{
                Util.print("\tNo architecture");
            }
        }
    }

    /**
     * Prints information of playground occupation on
     * the street. Prints the total length occupied and its
     * to street's length.
     */
    public static void printPlaygroundOccupationInfo() {
        for(Side side : Side.sides) {
            int totalPlaygroundLength = 0;

            for(int i = 0; i < street.getArchitectureCount(side); ++i) {
                Architecture currArchitecture = street.getArchitecture(i, side);

                if(currArchitecture instanceof Playground) {
                    totalPlaygroundLength += currArchitecture.getLength();
                }
            }

            Util.print("Playgrounds occupy total of " + totalPlaygroundLength + " length on the " + getSideName(side) + " side of the street.");
            Util.print("The ratio to total length is: " + (float) totalPlaygroundLength / street.getLength());
        }
    }

    /**
     * Prints information of building occupation on
     * the street. Prints the total length occupied and its
     * to street's length.
     */
    public static void printBuildingOccupationInfo() {
        for(Side side : Side.sides) {
            int totalBuildingLength = 0;

            for(int i = 0; i < street.getArchitectureCount(side); ++i) {
                Architecture currArchitecture = street.getArchitecture(i, side);

                if(currArchitecture instanceof Building) {
                    totalBuildingLength += currArchitecture.getLength();
                }
            }

            Util.print("Buildings occupy total of " + totalBuildingLength + " length on the " + getSideName(side) + " side of the street.");
            Util.print("The ratio to total length is: " + (float) totalBuildingLength / street.getLength());
        }
    }

    /**
     * Prints the silhouette of the street, as if
     * it's viewed from such angle that sides are
     * on top of another.
     * @param viewHeight    The maximum height of the view
     */
    public static void printStreetSilhouette(int viewHeight) {
        int viewWidth = street.getLength();

        // Characters as if pixels
        char[][] chars = new char[viewWidth][viewHeight];

        // Loop through all architectures
        for(Side side : Side.sides){
            for(int i = 0; i < street.getArchitectureCount(side); ++i){
                Architecture architecture = street.getArchitecture(i, side);

                int position = architecture.getPosition();
                int length = architecture.getLength();
                int height = architecture.getHeight();

                // Set every character in architecture's area
                // to '#'
                for(int x = 0; x < length; ++x){
                    for(int y = 0; y < height; ++y){
                        chars[position + x][y] = '#';
                    }
                }
            }
        }

        // Print the char matrix result
        // Silhouette is printed from up
        // to down, hence de starting y value
        for(int y = viewHeight - 1; y > -1; --y){
            for(int x = 0; x < viewWidth; ++x){
                char currChar = chars[x][y];

                // If character is default null char
                if(currChar == '\u0000'){
                    System.out.print(' ');
                }
                else{
                    System.out.print(currChar);
                }
            }
            Util.print();
        }

        // The street itself is printed as one
        // layer of '=' as long as the length
        // value of street
        for(int x = 0; x < viewWidth; ++x){
            System.out.print('=');
        }
        Util.print();

        // Prints something like a ruler to
        // make it easier to determine the
        // position
        for(int x = 0; x < viewWidth / 4 + ((viewHeight % 4 > 0) ? 1 : 0); ++x){
            System.out.print("|-4-");
        }
        System.out.print('|');
        Util.print();
    }

    /**
     * Works as one would imagine how <code>Side.toString()</code>
     * would work, no such method was implemented
     * because there's no real use of that in the
     * base program.
     * @param side  <code>Side</code> value
     * @return  'left' for <code>Side.Left</code> and 'right' for <code>Side.Right</code>
     */
    public static String getSideName(Side side) {
        return side == Side.Left ? "left" : "right";
    }
}
