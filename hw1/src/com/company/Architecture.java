package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Architecture is the abstract base class for all things that can
 * be constructed on a street and has properties of position, length,
 * and height and has a functionality of being focused on.
 *
 * @see Street
 * @see Positioned
 * @see Lengthed
 * @see Heighted
 * @see Focused
 */
public abstract class Architecture implements Positioned, Lengthed, Heighted, Focused {
    private int position;
    private int length;
    private int height;

    /**
     * Initializes the fields of this object with given values.
     * @param position  The position of the starting end of the architecture.
     * @param length    The length between the ends of the architecture.
     * @param height    The height of the architecture.
     * @throws InvalidAttributeValueException   When negative values are given as length or height.
     */
    public Architecture(int position, int length, int height) throws InvalidAttributeValueException {
        setPosition(position);
        setLength(length);
        setHeight(height);
    }

    /**
     * Checks if two architectures are overlapping over one another
     * as if they were on the same side of the street.
     * @param other The other architecture to be checked if it's overlapping with <code>this</code>
     *              instance of architecture or not.
     * @return  <code>true</code> if architectures overlap, <code>false</code> if not.
     */
    public boolean isOverlapping(Architecture other) {
        return (this.position < other.getPosition() + other.getLength()) && ((this.position + this.length) > other.getPosition());
    }

    /**
     * Returns the information about this architecture in the <code>String</code>
     * format of 'Architecture{position=..., length=..., height=...}'
     * @return  Architecture info <code>String</code>
     */
    @Override
    public String toString() {
        return "Architecture" + getArchitectureFieldsInfo();
    }

    @Override
    public abstract String focus();

    /**
     * Returns info about this architecture in the
     * form of '{position=..., length=..., height=...}'
     * @return  The info about this architecture
     *          consisting of its position, length,
     *          and height value.
     */
    public String getArchitectureFieldsInfo() {
        return "{" +
                "position=" + position +
                ", length=" + length +
                ", height=" + height +
                '}';
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void setLength(int length) throws InvalidAttributeValueException {
        if(length < 0){
            throw new InvalidAttributeValueException("Invalid length");
        }
        this.length = length;
    }

    @Override
    public void setHeight(int height) throws InvalidAttributeValueException {
        if(height < 0){
            throw new InvalidAttributeValueException("Invalid height");
        }
        this.height = height;
    }
}
