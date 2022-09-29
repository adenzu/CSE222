package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * AbstractStreet is the abstract class that contains and manages
 * all the common properties of differently implemented streets.
 */
public abstract class AbstractStreet implements Street {
    private int length;
    private final int[] occupiedLengths = {0, 0};
    private final int[] architectureCounts = {0, 0};

    /**
     * Creates a street with given value as its
     * length.
     * @param length    Length of the street.
     * @throws InvalidAttributeValueException   When given value for the length
     *                                          is negative.
     */
    public AbstractStreet(int length) throws InvalidAttributeValueException {
        setLength(length);
    }

    @Override
    public int getArchitectureCount(Side side) {
        return this.architectureCounts[side.toInt()];
    }

    @Override
    public int getOccupiedLength(Side side) {
        return this.occupiedLengths[side.toInt()];
    }

    @Override
    public int getRemainingLength(Side side) {
        return this.length - getOccupiedLength(side);
    }

    /**
     * Checks if given architecture's left end is inside this street.
     * @param architecture  Architecture to be checked
     * @return  <code>true</code>
     * @throws InvalidPositionException When architecture is outside this street.
     */
    protected boolean checkArchitecturePosition(Architecture architecture) throws InvalidPositionException {
        int position = architecture.getPosition();

        if(!Util.isInRange(0, this.length, position)){
            throw new InvalidPositionException();
        }

        return true;
    }

    /**
     * Checks if given architecture fits this street by checking position of its right end
     * if it's before where street ends or not.
     * @param architecture  Architecture to be checked
     * @return  <code>true</code>
     * @throws NotEnoughSpaceException  When architecture doesn't fit.
     */
    protected boolean checkArchitectureFits(Architecture architecture) throws NotEnoughSpaceException {
        // The other end of the architecture
        int endPosition = architecture.getPosition() + architecture.getLength();

        if(endPosition > this.length){
            throw new NotEnoughSpaceException();
        }

        return true;
    }

    /**
     * Increments the number of architectures on given side of the street by one.
     * @param side  Side of the street where a new architecture is built.
     */
    protected void incrementArchitectureCount(Side side) {
        this.architectureCounts[side.toInt()]++;
    }

    /**
     * Decrements the number of architectures on given side of the street by one.
     * @param side  Side of the street where an architecture is demolished.
     */
    protected void decrementArchitectureCount(Side side) {
        this.architectureCounts[side.toInt()]--;
    }

    /**
     * Changes amount of total occupied length of given side
     * by given change amount.
     * @param side      Side whose occupied length value will be changed
     * @param change    Change in occupied length
     */
    protected void changeOccupiedLength(Side side, int change) {
        this.occupiedLengths[side.toInt()] += change;
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
}
