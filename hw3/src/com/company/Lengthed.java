package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Lengthed is the interface for all the objects
 * that have length as property.
 */
public interface Lengthed {

    /**
     * Returns length value of this object.
     * @return  Length value of this object.
     */
    int getLength();

    /**
     * Sets length of this object to given value.
     * @param length    New length value for this object.
     * @throws InvalidAttributeValueException   When a negative value is given as length.
     */
    void setLength(int length) throws InvalidAttributeValueException;
}
