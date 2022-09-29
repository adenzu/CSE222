package com.company;

import javax.management.InvalidAttributeValueException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * LinkedListStreet is a street class that uses linkedlists
 * for architecture storation.
 */
public class LinkedListStreet extends AbstractStreet{
    private final LinkedList<LinkedList<Architecture>> architectures = new LinkedList<>();

    /**
     * Creates a street with given value as its
     * length.
     *
     * @param length Length of the street.
     * @throws InvalidAttributeValueException When given value for the length
     *                                        is negative.
     */
    public LinkedListStreet(int length) throws InvalidAttributeValueException {
        super(length);
        architectures.add(new LinkedList<>());
        architectures.add(new LinkedList<>());
    }

    @Override
    public boolean checkArchitectureValid(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException {
        checkArchitecturePosition(architecture);
        checkArchitectureFits(architecture);

        // Check if any other architecture collides
        for(Architecture other : architectures.get(side.toInt())){
            if(architecture.isOverlapping(other)){
                throw new PositionOccupiedException();
            }
        }

        return true;
    }

    @Override
    public void addArchitecture(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException {
        if(checkArchitectureValid(architecture, side)){
            // Add architecture
            architectures.get(side.toInt()).addFirst(architecture);

            // Update street
            incrementArchitectureCount(side);
            changeOccupiedLength(side, architecture.getLength());
        }
    }

    @Override
    public Architecture getArchitecture(int index, Side side) throws IndexOutOfBoundsException {
        return architectures.get(side.toInt()).get(index);
    }

    @Override
    public void removeArchitecture(Architecture architecture) throws ArchitectureNotFoundException {
        Iterator<LinkedList<Architecture>> it = architectures.iterator();

        // Try removing from left
        if(it.next().remove(architecture)){
            decrementArchitectureCount(Side.Left);
            changeOccupiedLength(Side.Left, -architecture.getLength());
        }
        // Try removing from right
        else if(it.next().remove(architecture)){
            decrementArchitectureCount(Side.Right);
            changeOccupiedLength(Side.Right, -architecture.getLength());
        }

        throw new ArchitectureNotFoundException();
    }

    @Override
    public void removeArchitecture(int index, Side side) throws IndexOutOfBoundsException {
        // Remove architecture
        int length = architectures.get(side.toInt()).remove(index).getLength();

        // Update street
        decrementArchitectureCount(side);
        changeOccupiedLength(side, -length);
    }
}
