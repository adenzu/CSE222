package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Playground is the subclass of architecture that
 * has a default height value.
 */
public class Playground extends Architecture{
    private static final int DEFAULT_HEIGHT = 4;

    /**
     * Creates a playground with given values.
     * @param position  The position of the starting end of this playground.
     * @param length    The length between the ends of this playground.
     * @throws InvalidAttributeValueException When length value is negative.
     */
    public Playground(int position, int length) throws InvalidAttributeValueException {
        super(position, length, DEFAULT_HEIGHT);
    }

    /**
     * Returns the information about this playground in the <code>String</code>
     * format of 'Playground{position=..., length=..., height=...}'
     * @return  Playground info <code>String</code>
     */
    @Override
    public String toString() {
        return "Playground" + getArchitectureFieldsInfo();
    }

    /**
     * Returns the length info of the playground.
     * @return  Length of the playground in <code>String</code>.
     */
    @Override
    public String focus() {
        return "Playground length: " + getLength();
    }
}
