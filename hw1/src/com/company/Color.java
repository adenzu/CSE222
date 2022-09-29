package com.company;

import javax.management.InvalidAttributeValueException;

/**
 * Color is a class for representing colors and consists of
 * red, green, blue, and alpha channel integer values in range
 * of 0 to 255 including.
 */
public class Color {
    private int r, g, b, a;

    /**
     * Creates an opaque color with given values.
     * @param r Red value of this object.
     * @param g Green value of this object.
     * @param b Blue value of this object.
     * @throws InvalidAttributeValueException   When any given value is not in range 0-255 included.
     */
    public Color(int r, int g, int b) throws InvalidAttributeValueException {
        this(r, g, b, 255);
    }

    /**
     * Creates a color with given values.
     * @param r Red value of this object.
     * @param g Green value of this object.
     * @param b Blue value of this object.
     * @param a Alpha value of this object.
     * @throws InvalidAttributeValueException   When any given value is not in range 0-255 included.
     */
    public Color(int r, int g, int b, int a) throws InvalidAttributeValueException {
        setR(r);
        setG(g);
        setB(b);
        setA(a);
    }

    /**
     * Returns the information about this color in the form of
     * 'Color{r=..., g=..., b=..., a=...}'
     * @return  Info about the color.
     */
    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

    /**
     * Returns the red channel value of this object.
     * @return  Red channel value of this object.
     */
    public int getR() {
        return r;
    }

    /**
     * Returns the green channel value of this object.
     * @return  Green channel value of this object.
     */
    public int getG() {
        return g;
    }

    /**
     * Returns the blue channel value of this object.
     * @return  Blue channel value of this object.
     */
    public int getB() {
        return b;
    }

    /**
     * Returns the alpha channel value of this object.
     * @return  Alpha channel value of this object.
     */
    public int getA() {
        return a;
    }

    /**
     * Sets the red channel of this object to given value.
     * @param r New red channel value for this object.
     * @throws InvalidAttributeValueException   When given value is not in range of 0-255 inclusive.
     */
    public void setR(int r) throws InvalidAttributeValueException {
        if(!isValid(r)) throw new InvalidAttributeValueException();
        this.r = r;
    }

    /**
     * Sets the green channel of this object to given value.
     * @param g New red channel value for this object.
     * @throws InvalidAttributeValueException   When given value is not in range of 0-255 inclusive.
     */
    public void setG(int g) throws InvalidAttributeValueException {
        if(!isValid(g)) throw new InvalidAttributeValueException();
        this.g = g;
    }

    /**
     * Sets the blue channel of this object to given value.
     * @param b New red channel value for this object.
     * @throws InvalidAttributeValueException   When given value is not in range of 0-255 inclusive.
     */
    public void setB(int b) throws InvalidAttributeValueException {
        if(!isValid(b)) throw new InvalidAttributeValueException();
        this.b = b;
    }

    /**
     * Sets the alpha channel of this object to given value.
     * @param a New red channel value for this object.
     * @throws InvalidAttributeValueException   When given value is not in range of 0-255 inclusive.
     */
    public void setA(int a) throws InvalidAttributeValueException {
        if(!isValid(a)) throw new InvalidAttributeValueException();
        this.a = a;
    }

    private boolean isValid(int value) {
        return Util.isInRange(0, 255, value);
    }
}
