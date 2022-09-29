package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * AbstractStreet is the base class for street interface
 * that has functionalities that are same for every
 * extension of this class. Such as <code>getArchitectureCount</code>
 */
public class OldStreet extends AbstractStreet{
    private static final int MIN_SIZE = 8;
    private final Architecture[][] architectures = new Architecture[Side.sides.length][MIN_SIZE];

    /**
     * Creates a street with given value as its
     * length.
     * @param length    Length of the 
     * @throws InvalidAttributeValueException   When given value for the length
     *                                          is negative.
     */
    public OldStreet(int length) throws InvalidAttributeValueException {
        super(length);
    }

    @Override
    public boolean checkArchitectureValid(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException {
        checkArchitecturePosition(architecture);
        checkArchitectureFits(architecture);

        int architectureCount = getArchitectureCount(side);
        Architecture[] architecturesOnSide = getArchitectures(side);

        // Loop through all the architectures on the given side to
        // check if anyone of them clashes with the given architecture
        for(int i = 0; i < architectureCount; ++i){
            if(architecture.isOverlapping(architecturesOnSide[i])){
                throw new InvalidPositionException();
            }
        }
        
        return true;
    }

    public void addArchitecture(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException {
        if(!checkArchitectureValid(architecture, side)) return;

        Architecture[] architecturesOnSide = getArchitectures(side);

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
        changeOccupiedLength(side, architecture.getLength());
    }

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
                }
            }

            // If architecture is not found on
            // this side of the street check
            // the other side
            if(!removed) continue;

            // Update number of architectures on this side
            decrementArchitectureCount(side);

            // Update occupied length on this side
            changeOccupiedLength(side, -architecture.getLength());

            shrink(side);

            // Operation was successful break the loop
            return;
        }

        // Architecture was not found on any side of the street
        throw new ArchitectureNotFoundException();
    }

    public Architecture getArchitecture(int index, Side side) throws IndexOutOfBoundsException {
        if(!Util.isInRange(0, getArchitectureCount(side) - 1, index)){
            throw new IndexOutOfBoundsException();
        }
        return getArchitectures(side)[index];
    }

    public void removeArchitecture(int index, Side side) throws IndexOutOfBoundsException {
        if(!Util.isInRange(0, getArchitectureCount(side) - 1, index)) throw new IndexOutOfBoundsException();

        Architecture[] architectures = getArchitectures(side);

        decrementArchitectureCount(side);
        changeOccupiedLength(side, -architectures[index].getLength());
        architectures[index] = null;

        if (architectures.length - (index + 1) >= 0){
            System.arraycopy(architectures, index + 1, getArchitectures(side), index, architectures.length - (index + 1));
        }

        shrink(side);
    }

    private void shrink(Side side) {
        Architecture[] architectures = getArchitectures(side);

        int oldLength = architectures.length;
        int newLength = oldLength / 2;

        // Check if the array can be shrunk
        while(MIN_SIZE < newLength && getArchitectureCount(side) < newLength){
            newLength /= 2;
        }

        newLength *= 2;

        // If the new possible array length is lesser
        if(newLength < oldLength){
            // Reallocate
            this.architectures[side.toInt()] = new Architecture[newLength];
            System.arraycopy(architectures, 0, getArchitectures(side), 0, newLength);
        }
    }

    private Architecture[] getArchitectures(Side side) {
        return this.architectures[side.toInt()];
    }
}
