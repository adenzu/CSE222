package com.company;

/**
 * Data structure for two dimensional position data.
 */
public class Vector2 {
    private float x, y;

    /**
     * Constructs with given horizontal and vertical values of position.
     * @param x Horizontal value.
     * @param y Vertical value.
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Scales the position values by given multiplier.
     * @param scalar    The multiplying factor.
     * @return          This vector after scaling.
     */
    public Vector2 scale(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * Sums two vectors and returns result.
     * @param vec1  One of the vectors to be summed.
     * @param vec2  Other one of the vectors to be summed.
     * @return      Result of sum.
     */
    public static Vector2 sum(Vector2 vec1, Vector2 vec2) {
        return new Vector2(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    /**
     * @return X value of this position.
     */
    public float getX() {
        return x;
    }

    /**
     * @return Y value of this position.
     */
    public float getY() {
        return y;
    }

    /**
     * Sets x value of this position.
     * @param x New x value of this position.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets y value of this position.
     * @param y New y value of this position.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return <code>String</code> representation of this position.
     */
    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
