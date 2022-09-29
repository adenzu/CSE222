package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Market is the subclass of the building that has
 * an opening and a closing time properties.
 *
 * @see Building
 * @see Time
 */
public class Market extends Building {
    private Time openTime;
    private Time closeTime;

    /**
     * Creates a market object with given values.
     * @param position  The position of the starting end of this market.
     * @param length    The length between the ends of this market.
     * @param height    The height of this market.
     * @param owner     The owner of this market.
     * @param openTime  The opening time of this market.
     * @param closeTime The closing time of this market.
     * @throws InvalidAttributeValueException   When either of given length and height value
     *                                          is negative.
     */
    public Market(int position, int length, int height, Owner owner, Time openTime, Time closeTime) throws InvalidAttributeValueException {
        super(position, length, height, owner);
        setOpenTime(openTime);
        setCloseTime(closeTime);
    }

    /**
     * Returns the information about this market in the <code>String</code>
     * format of 'Market{position=..., length=..., height=...}'
     * @return  Market info <code>String</code>
     */
    @Override
    public String toString() {
        return "Market" + getArchitectureFieldsInfo();
    }

    /**
     * Returns <code>String</code> of market closing time.
     * @return  Closing time of this market.
     */
    @Override
    public String focus() {
        return "Market closing time: " + this.closeTime.toString();
    }

    /**
     * Returns the time that this market opens.
     * @return  Opening time of this market.
     */
    public Time getOpenTime() {
        return openTime;
    }

    /**
     * Returns the time that this market closes.
     * @return  Closing time of this market.
     */
    public Time getCloseTime() {
        return closeTime;
    }

    /**
     * Sets the opening time of this market.
     * @param openTime  New opening time for this market.
     */
    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    /**
     * Sets the closing time of this market.
     * @param closeTime New closing time for this market.
     */
    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }
}
