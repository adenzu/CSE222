package com.company;

/**
 * Street is the class that contains and manages
 * all the architectures built on it. It also has
 * a finite length so the room for architectures
 * is finite.
 *
 * @see Architecture
 */
public interface Street extends Lengthed {

    /**
     * Checks if given architecture can be built on given side.
     * @param architecture  Architecture to be checked if it can be built.
     * @param side          Side of the street for architecture to be checked
     *                      if it could be built on.
     * @return              <code>true</code> if architecture can be built.
     *
     * @throws InvalidPositionException     When position of architecture is invalid.
     * @throws NotEnoughSpaceException      When architecture doesn't fit the street.
     * @throws PositionOccupiedException    When there's another architecture occupying
     *                                      given architecture's space.
     */
    boolean checkArchitectureValid(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException;

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
    void addArchitecture(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException;

    /**
     * Returns the architecture on the given side of this
     * street with the given index.
     * @param index Index of the architecture to be returned.
     * @param side  Side of this street that the architecture
     *              is on.
     * @return      Architecture at given index on given side.
     * @throws IndexOutOfBoundsException   When given index is
     *                                          outside the array size.
     */
    Architecture getArchitecture(int index, Side side) throws IndexOutOfBoundsException;

    /**
     * Removes given architecture from the street on given side.
     * @param architecture  Architecture to be removed.
     * @throws ArchitectureNotFoundException    When given architecture is
     *                                          nowhere to be found on this
     *                                          street.
     */
    void removeArchitecture(Architecture architecture) throws ArchitectureNotFoundException;

    /**
     * Removes the architecture at the given index on
     * the given side of this street.
     * @param index Index of the architecture to be removed.
     * @param side  Side of this street that the architecture
     *              is on.
     * @throws IndexOutOfBoundsException   When given index is
     *                                          outside the array size.
     */
    void removeArchitecture(int index, Side side) throws IndexOutOfBoundsException;

    /**
     * Returns the number of architecture on the given side of this street.
     * @param side  Side of the street to count number of architectures.
     * @return      Number of architectures on the given side of this street.
     */
    int getArchitectureCount(Side side);

    /**
     * Returns the total amount of length that is occupied by architectures
     * on given side of this street.
     * @param side  Side of the street to count total occupied length.
     * @return      Total amount of length occupied.
     */
    int getOccupiedLength(Side side);

    /**
     * Returns the total amount of remaining length that is available for
     * architectures on given side of this street.
     * @param side  Side of the street to count total available length.
     * @return      Total amount of length available.
     */
    int getRemainingLength(Side side);

    /**
     * Side is the enum for setting apart
     * the architectures on a street that
     * are built on different sides of it
     */
    enum Side {
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
    class NotEnoughSpaceException extends Exception {

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
    class InvalidPositionException extends Exception {

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
    class PositionOccupiedException extends Exception {

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
    class ArchitectureNotFoundException extends Exception {

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
