package com.company;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;

/**
 * ArrayListStreet is a street class that uses
 * arraylist for storing architectures.
 */
public class ArrayListStreet extends AbstractStreet{
    private final int DEFAULT_SIZE = 8;
    private final ArrayList<ArrayList<Architecture>> architectures = new ArrayList<>(2);

    /**
     * Creates a street with given value as its
     * length.
     *
     * @param length Length of the street.
     * @throws InvalidAttributeValueException When given value for the length
     *                                        is negative.
     */
    public ArrayListStreet(int length) throws InvalidAttributeValueException {
        super(length);
        architectures.add(new ArrayList<>(DEFAULT_SIZE));
        architectures.add(new ArrayList<>(DEFAULT_SIZE));
    }

    @Override
    public boolean checkArchitectureValid(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException {
        checkArchitecturePosition(architecture);
        checkArchitectureFits(architecture);

        // Check if any architecture overlaps
        for(Architecture other : architectures.get(side.toInt())){
            if(architecture.isOverlapping(other)){
                throw new PositionOccupiedException();
            }
        }

        return true;
    }

    @Override
    public void addArchitecture(Architecture architecture, Side side) throws PositionOccupiedException, NotEnoughSpaceException, InvalidPositionException {
        if(checkArchitectureValid(architecture, side)){
            architectures.get(side.toInt()).add(architecture);
            incrementArchitectureCount(side);
            changeOccupiedLength(side, architecture.getLength());
        }
    }

    @Override
    public Architecture getArchitecture(int index, Side side) {
        return architectures.get(side.toInt()).get(index);
    }

    @Override
    public void removeArchitecture(Architecture architecture) throws ArchitectureNotFoundException {

        // Remove on left side
        if(architectures.get(0).remove(architecture)){
            decrementArchitectureCount(Side.Left);
            changeOccupiedLength(Side.Left, -architecture.getLength());
        }
        // If didn't succeed remove on right side
        else if(architectures.get(1).remove(architecture)){
            decrementArchitectureCount(Side.Right);
            changeOccupiedLength(Side.Right, -architecture.getLength());
        }

        throw new ArchitectureNotFoundException();
    }

    @Override
    public void removeArchitecture(int index, Side side) {
        int length = architectures.get(side.toInt()).remove(index).getLength();
        decrementArchitectureCount(side);
        changeOccupiedLength(side, length);
    }
}
