package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Time keeps three integer values as hours,
 * minutes, and seconds. These values are not
 * designed to work as AM and/or PM so range
 * for hours is 0-23.
 */
public class Time {
    private int hours;
    private int minutes;
    private int seconds;

    /**
     * Creates a time object with given values.
     * @param hours     Hours in range of 0-23, included.
     * @param minutes   Minutes in range of 0-59, included.
     * @param seconds   Seconds in range of 0-59, included.
     * @throws InvalidAttributeValueException   When out of range value is given.
     */
    protected Time(int hours, int minutes, int seconds) throws InvalidAttributeValueException {
        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
    }

    /**
     * @return  <code>String</code> of this time in 'HOURS:MINUTES' format
     *          if seconds attribute is 0, 'HOURS:MINUTES:SECONDS' if not
     */
    @Override
    public String toString() {
        String result = this.hours < 10 ? "0" : "";
        result += this.hours;

        result += ":" + (this.minutes < 10 ? "0" : "");
        result += this.minutes;

        if(this.seconds != 0){
            result += ":" + (this.seconds < 10 ? "0" : "");
            result += this.seconds;
        }

        return result;
    }

    /**
     * @return Hours of this time in <code>int</code>.
     */
    public int getHours() {
        return hours;
    }

    /**
     * @return Minutes of this time in <code>int</code>.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * @return Seconds of this time in <code>int</code>.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Sets hours of this time to given value.
     * @param hours New hours for this time.
     * @throws InvalidAttributeValueException   When given value is not in the range 0-23 included.
     */
    public void setHours(int hours) throws InvalidAttributeValueException {
        if(!Util.isInRange(0, 23, hours)){
            throw new InvalidAttributeValueException("Invalid hour");
        }
        this.hours = hours;
    }

    /**
     * Sets minutes of this time to given value.
     * @param minutes New minutes for this time.
     * @throws InvalidAttributeValueException   When given value is not in the range 0-59 included.
     */
    public void setMinutes(int minutes) throws InvalidAttributeValueException {
        if(!Util.isInRange(0, 59, minutes)){
            throw new InvalidAttributeValueException("Invalid minute");
        }
        this.minutes = minutes;
    }

    /**
     * Sets seconds of this time to given value.
     * @param seconds New seconds for this time.
     * @throws InvalidAttributeValueException   When given value is not in the range 0-59 included.
     */
    public void setSeconds(int seconds) throws InvalidAttributeValueException {
        if(!Util.isInRange(0, 59, seconds)){
            throw new InvalidAttributeValueException("Invalid second");
        }
        this.seconds = seconds;
    }
}
