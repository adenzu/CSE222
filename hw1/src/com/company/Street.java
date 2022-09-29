package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Street is the class that contains and manages
 * all the architectures built on it. It also has
 * a finite length so the room for architectures
 * is finite.
 *
 * @see Architecture
 */
public class Street implements Lengthed {
    private static final int MIN_SIZE = 8;

    private int length;
    private final int[] occupiedLengths = {0, 0};
    private final int[] architectureCounts = {0, 0};
    private final Architecture[][] architectures = new Architecture[Side.sides.length][MIN_SIZE];

    /**
     * Creates a street with given value as its
     * length.
     * @param length    Length of the street.
     * @throws InvalidAttributeValueException   When given value for the length
     *                                          is negative.
     */
    public Street(int length) throws InvalidAttributeValueException {
        setLength(length);
    }

    /**
     * Adds the given architecture to the street
     * on the side that is also given. Architecture is
     * added on the end of the architectures array.
     * @param architecture  An architecture object to be added to this street.
     * @param side          The side of the street for architecture to be added.
     * @throws InvalidPositionException     When position of the architecture
     *                                      is outside the street.
     * @throws NotEnoughSpaceException      When given architecture does not fit
     *                                      the street.
     * @throws PositionOccupiedException    When given architectures clashes with
     *                                      another architecture.
     */
    public void addArchitecture(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException {
        int position = architecture.getPosition();

        if(!Util.isInRange(0, this.length, position)){
            throw new InvalidPositionException();
        }

        // The other end of the architecture
        int endPosition = position + architecture.getLength();

        if(endPosition > this.length){
            throw new NotEnoughSpaceException();
        }

        int architectureCount = getArchitectureCount(side);
        Architecture[] architecturesOnSide = getArchitectures(side);

        // Loop through all the architectures on the given side to
        // check if anyone of them clashes with the given architecture
        for(int i = 0; i < architectureCount; ++i){
            if(architecture.isOverlapping(architecturesOnSide[i])){
                throw new PositionOccupiedException();
            }
        }

        // Reallocate if necessary
        if(getArchitectureCount(side) == architecturesOnSide.length){
            this.architectures[side.toInt()] = new Architecture[architecturesOnSide.length * 2];
            System.arraycopy(architecturesOnSide, 0, getArchitectures(side), 0, architecturesOnSide.length);
        }

        // Add to end
        getArchitectures(side)[getArchitectureCount(side)] = architecture;

        // Increment number of architectures on this side
        incrementArchitectureCount(side);

        // Update the occupied length of this side
        this.occupiedLengths[side.toInt()] += architecture.getLength();
    }

    /**
     * Removes given architecture from the street on given side.
     * @param architecture  Architecture to be removed.
     * @throws ArchitectureNotFoundException    When given architecture is
     *                                          nowhere to be found on this
     *                                          street.
     */
    public void removeArchitecture(Architecture architecture) throws ArchitectureNotFoundException {
        boolean removed = false;

        // Check each side
        for(Side side : Side.sides){
            Architecture[] architecturesOnSide = getArchitectures(side);

            // Check for every architecture
            for(int i = 0; i < getArchitectureCount(side); ++i){
                // If matched
                if(architecturesOnSide[i] == architecture){
                    architecturesOnSide[i] = null;
                    removed = true;
                }
                // Else if removal already happened
                // therefore previous element is null
                else if(removed){
                    // Move this element to one before
                    architecturesOnSide[i - 1] = architecturesOnSide[i];
                    architecturesOnSide[i] = null;
                }
            }

            // If architecture is not found on
            // this side of the street check
            // the other side
            if(!removed) continue;

            // Update number of architectures on this side
            decrementArchitectureCount(side);

            // Update occupied length on this side
            this.occupiedLengths[side.toInt()] -= architecture.getLength();

            int oldLength = architecturesOnSide.length;
            int newLength = oldLength;

            // Check if the array can be shrunk
            while(MIN_SIZE < newLength / 2 && getArchitectureCount(side) < newLength / 2){
                newLength /= 2;
            }

            // If the new possible array length is lesser
            if(newLength < oldLength){
                // Reallocate
                this.architectures[side.toInt()] = new Architecture[newLength];
                System.arraycopy(architecturesOnSide, 0, getArchitectures(side), 0, newLength);
            }

            // Operation was successful break the loop
            return;
        }

        // Architecture was not found on any side of the street
        throw new ArchitectureNotFoundException();
    }

    /**
     * Returns the architecture on the given side of this
     * street with the given index.
     * @param index Index of the architecture to be returned.
     * @param side  Side of this street that the architecture
     *              is on.
     * @return      Architecture at given index on given side.
     * @throws ArrayIndexOutOfBoundsException   When given index is
     *                                          outside the array size.
     */
    public Architecture getArchitecture(int index, Side side) throws ArrayIndexOutOfBoundsException {
        if(!Util.isInRange(0, getArchitectureCount(side) - 1, index)){
            throw new ArrayIndexOutOfBoundsException();
        }
        return getArchitectures(side)[index];
    }

    /**
     * Removes the architecture at the given index on
     * the given side of this street.
     * @param index Index of the architecture to be removed.
     * @param side  Side of this street that the architecture
     *              is on.
     * @throws ArrayIndexOutOfBoundsException   When given index is
     *                                          outside the array size.
     */
    public void removeArchitectureAt(int index, Side side) throws ArrayIndexOutOfBoundsException {
        if(!Util.isInRange(0, getArchitectureCount(side) - 1, index)) throw new ArrayIndexOutOfBoundsException();

        try{
            removeArchitecture(getArchitecture(index, side));
        }
        catch (Exception ignored){
            // impossible
        }
    }

    /**
     * Returns the number of architecture on the given side of this street.
     * @param side  Side of the street to count number of architectures.
     * @return      Number of architectures on the given side of this street.
     */
    public int getArchitectureCount(Side side) {
        return this.architectureCounts[side.toInt()];
    }

    /**
     * Returns the total amount of length that is occupied by architectures
     * on given side of this street.
     * @param side  Side of the street to count total occupied length.
     * @return      Total amount of length occupied.
     */
    public int getOccupiedLength(Side side) {
        return this.occupiedLengths[side.toInt()];
    }

    /**
     * Returns the total amount of remaining length that is available for
     * architectures on given side of this street.
     * @param side  Side of the street to count total available length.
     * @return      Total amount of length available.
     */
    public int getRemainingLength(Side side) {
        return this.length - getOccupiedLength(side);
    }

    private Architecture[] getArchitectures(Side side) {
        return this.architectures[side.toInt()];
    }

    private void incrementArchitectureCount(Side side) {
        this.architectureCounts[side.toInt()]++;
    }

    private void decrementArchitectureCount(Side side) {
        this.architectureCounts[side.toInt()]--;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) throws InvalidAttributeValueException {
        if(length < 0) throw new InvalidAttributeValueException("Invalid length");
        this.length = length;
    }

    /**
     * Side is the enum for setting apart
     * the architectures on a street that
     * are built on different sides of it
     */
    public enum Side {
        /**
         * Left side of the street
         */
        Left(0),

        /**
         * Right side of the street
         */
        Right(1);

        /**
         * Sides of the street in an array
         */
        public static final Side[] sides = {Left, Right};

        private final int value;

        Side(int value) {
            this.value = value;
        }

        /**
         * @return The <code>int</code> value of this side.
         */
        public int toInt() {
            return this.value;
        }
    }

    /**
     * Exception for when there's not enough space in
     * the street for an instance of architecture
     * build.
     */
    public static class NotEnoughSpaceException extends Exception {

        /**
         * Initializes with the message: 'Architecture is partially outside street boundaries'.
         */
        public NotEnoughSpaceException() {
            super("Architecture is partially outside street boundaries");
        }

        /**
         * Initializes with given message.
         * @param message   Message of the exception.
         */
        public NotEnoughSpaceException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when an architecture is
     * tried to be built outside a street.
     */
    public static class InvalidPositionException extends Exception {

        /**
         * Initializes with the message: 'Position of the architecture is outside the street'.
         */
        public InvalidPositionException() {
            super("Position of the architecture is outside the street");
        }

        /**
         * Initializes with given message.
         * @param message   Message of the exception.
         */
        public InvalidPositionException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when there's another architecture in
     * the street for an instance of architecture
     * build at a given position.
     */
    public static class PositionOccupiedException extends Exception {

        /**
         * Initializes with the message: 'Area is partially or altogether occupied by another architecture'.
         */
        public PositionOccupiedException() {
            super("Area is partially or altogether occupied by another architecture");
        }

        /**
         * Initializes with given message.
         * @param message   Message of the exception.
         */
        public PositionOccupiedException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when an architecture is wanted to
     * be removed from a street but there's no such
     * architecture on the street.
     */
    public static class ArchitectureNotFoundException extends Exception {

        /**
         * Initializes with the message: 'No such architecture was found'.
         */
        public ArchitectureNotFoundException() {
            super("No such architecture was found");
        }

        /**
         * Initializes with given message.
         * @param message   Message of the exception.
         */
        public ArchitectureNotFoundException(String message) {
            super(message);
        }
    }
}
