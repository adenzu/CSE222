package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Heighted is the interface for all objects
 * that have height property.
 */
public interface Heighted {

    /**
     * Returns height value of this object.
     * @return  Height value of this object.
     */
    int getHeight();

    /**
     * Sets this objects height to given value.
     * @param height    New height value for this object.
     * @throws InvalidAttributeValueException   When a negative value is given as height.
     */
    void setHeight(int height) throws InvalidAttributeValueException;
}
