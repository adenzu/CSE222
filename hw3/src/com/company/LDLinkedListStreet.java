package com.company;

import javax.management.InvalidAttributeValueException;
import java.util.Iterator;

/**
 * LDLinkedListStreet is a street class that uses
 * LDLinkedList for storation of architectures.
 */
public class LDLinkedListStreet extends AbstractStreet{
    private final LDLinkedList<LDLinkedList<Architecture>> architectures = new LDLinkedList<>();

    /**
     * Creates a street with given value as its
     * length.
     *
     * @param length Length of the street.
     * @throws InvalidAttributeValueException When given value for the length
     *                                        is negative.
     */
    public LDLinkedListStreet(int length) throws InvalidAttributeValueException {
        super(length);
        architectures.add(new LDLinkedList<>());
        architectures.add(new LDLinkedList<>());
    }

    @Override
    public boolean checkArchitectureValid(Architecture architecture, Side side) throws InvalidPositionException, NotEnoughSpaceException, PositionOccupiedException {
        checkArchitecturePosition(architecture);
        checkArchitectureFits(architecture);

        // Check if any architecture occupies the given architecture's position
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
            architectures.get(side.toInt()).add(architecture);

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
        Iterator<LDLinkedList<Architecture>> it = architectures.iterator();

        // Try removing from left side
        if(it.next().remove(architecture)){
            decrementArchitectureCount(Side.Left);
            changeOccupiedLength(Side.Left, -architecture.getLength());
        }
        // Try removing from right side
        else if(it.next().remove(architecture)){
            decrementArchitectureCount(Side.Right);
            changeOccupiedLength(Side.Right, -architecture.getLength());
        }

        throw new ArchitectureNotFoundException();
    }

    @Override
    public void removeArchitecture(int index, Side side) throws IndexOutOfBoundsException {
        // Remove architecture
        Architecture architecture = architectures.get(side.toInt()).remove(index);

        // Update street
        decrementArchitectureCount(side);
        changeOccupiedLength(side, -architecture.getLength());
    }
}
