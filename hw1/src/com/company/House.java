package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * House is the subclass of the Building that has unique
 * properties of number of rooms and color.
 *
 * @see Building
 * @see Color
 */
public class House extends Building {
    private int roomCount;
    private Color color;

    /**
     * Creates a house object with given values.
     * @param position  The position of the starting end of this house.
     * @param length    The length between the ends of this house.
     * @param height    The height of this house.
     * @param owner     The owner of this house.
     * @param roomCount The number of rooms of this house.
     * @param color     The color of this house.
     * @throws InvalidAttributeValueException   When negative values are given as length or height.
     */
    public House(int position, int length, int height, Owner owner, int roomCount, Color color) throws InvalidAttributeValueException {
        super(position, length, height, owner);
        setRoomCount(roomCount);
        setColor(color);
    }

    /**
     * Returns the information about this house in the <code>String</code>
     * format of 'House{position=..., length=..., height=...}'
     * @return  House info <code>String</code>
     */
    @Override
    public String toString() {
        return "House" + getArchitectureFieldsInfo();
    }

    /**
     * Returns the <code>String</code> of this object's owner.
     * @return  <code>String</code> of owner of this object.
     */
    @Override
    public String focus() {
        return "House owner: " + getOwner().getName();
    }

    /**
     * Returns the number of rooms of this object.
     * @return  Number of rooms of this object.
     */
    public int getRoomCount() {
        return roomCount;
    }

    /**
     * Returns the color of this object.
     * @return  Color of this object.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets number of rooms of this object to given value.
     * @param roomCount New number of rooms value for this object.
     * @throws InvalidAttributeValueException   When a negative value is given.
     */
    public void setRoomCount(int roomCount) throws InvalidAttributeValueException {
        if(roomCount < 1){
            throw new InvalidAttributeValueException("Number of rooms has to be positive");
        }
        this.roomCount = roomCount;
    }

    /**
     * Sets color of this object to given value.
     * @param color New color value for this object.
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
