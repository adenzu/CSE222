package com.company;

/**
 * Boundary of area contained by several vertices.
 */
public interface Boundary {
    /**
     * Checks if given position is inside this boundary.
     * @param point Position to be checked.
     * @return      <code>true</code> if <code>point</code> is inside, <code>false</code> otherwise.
     */
    boolean isInside(Vector2 point);

    /**
     * @return Number of vertices there are in the boundary.
     */
    int verticesCount();

    /**
     * @param index Index of the vertex to be returned.
     * @return      <code>Vector2</code> position of the vertex with given index.
     */
    Vector2 vertexAt(int index);

    /**
     * @return The array of vertex positions.
     */
    Vector2[] getVertices();
}
