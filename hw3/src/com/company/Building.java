package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Building is the abstract base class for all things
 * that are architecture and have an owner.
 *
 * @see Architecture
 * @see Owner
 */
public abstract class Building extends Architecture {
    private Owner owner;

    /**
     * Initializes the fields of this object with given values.
     * @param position  The position of the starting end of the building.
     * @param length    The length between the ends of the building.
     * @param height    The height of the building.
     * @param owner     The owner of the building.
     * @throws InvalidAttributeValueException   When negative values are given as length or height.
     */
    public Building(int position, int length, int height, Owner owner) throws InvalidAttributeValueException {
        super(position, length, height);
        setOwner(owner);
    }

    /**
     * Returns the information about this building in the <code>String</code>
     * format of 'Building{position=..., length=..., height=...}'
     * @return  Building info <code>String</code>
     */
    @Override
    public String toString() {
        return "Building" + getArchitectureFieldsInfo();
    }

    /**
     * Returns the owner of this building.
     * @return  Owner of this building.
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this building.
     * @param owner New owner of this building.
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
