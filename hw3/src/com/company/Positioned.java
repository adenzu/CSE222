package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Positioned is the interface for all objects
 * that have a position value.
 */
public interface Positioned {

    /**
     * Returns position value of this object.
     * @return  Position value of this object.
     */
    int getPosition();

    /**
     * Sets position of this object to given value.
     * @param position  New length value for this object.
     */
    void setPosition(int position);
}
