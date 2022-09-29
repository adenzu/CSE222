package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Office is the subclass of building and has
 * an array of <code>String</code> as types of jobs as a
 * property.
 */
public class Office extends Building {
    private final String[] jobTypes;

    /**
     * Creates a market object with given values.
     * @param position  The position of the starting end of this office.
     * @param length    The length between the ends of this office.
     * @param height    The height of this office.
     * @param owner     The owner of this office.
     * @param jobTypes  The array of <code>String</code> consisting of
     *                  job types that are present in this office.
     * @throws InvalidAttributeValueException   When either of given length and height
     *                                          values is negative.
     */
    public Office(int position, int length, int height, Owner owner, String[] jobTypes) throws InvalidAttributeValueException {
        super(position, length, height, owner);
        this.jobTypes = jobTypes;
    }

    /**
     * Returns the information about this office in the <code>String</code>
     * format of 'Office{position=..., length=..., height=...}'
     * @return  Office info <code>String</code>
     */
    @Override
    public String toString() {
        return "Office" + getArchitectureFieldsInfo();
    }

    /**
     * Returns list of job types present in this office
     * object in <code>String</code> format.
     * @return  List of job types present in this office.
     */
    @Override
    public String focus() {
        String result = "Office job types: ";

        for(String jobType : this.jobTypes){
            result += "\n" + jobType;
        }

        return result;
    }
}
