package com.company;

import java.util.Arrays;

/**
 * Boundary object for checking inside and outside positions.
 */
public class RectangleBoundary implements Boundary{
    private final Vector2[] vertices;

    /**
     * Constructs a boundary with given two opposite corners.
     * @param corner1   One of the corners.
     * @param corner2   Other one of the corners.
     */
    public RectangleBoundary(Vector2 corner1, Vector2 corner2) {
        vertices = new Vector2[]{corner1, corner2};
    }

    @Override
    public boolean isInside(Vector2 point) {
        return  (point.getX() - vertices[0].getX()) * (point.getX() - vertices[1].getX()) <= 0 &&
                (point.getY() - vertices[0].getY()) * (point.getY() - vertices[1].getY()) <= 0;
    }

    @Override
    public int verticesCount() {
        return vertices.length;
    }

    @Override
    public Vector2[] getVertices() {
        return vertices;
    }

    @Override
    public Vector2 vertexAt(int index) {
        return vertices[index];
    }

    @Override
    public String toString() {
        return "RectangleBoundary{" +
                "vertices=" + Arrays.toString(vertices) +
                '}';
    }
}
