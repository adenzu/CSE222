package com.company;

/**
 * Data structure that consists of nodes that have 4 children nodes. Implemented as binary tree.
 */
public class QuadTree {
    private Vector2 value;
    private QuadTree subArea;
    private final Boundary boundary;
    private final QuadTree nextPart;

    /**
     * Constructs a quadtree with square area with given range of coordinates/positions.
     * @param min   The least value that a successful position addition can be done.
     * @param max   The greatest value that a successful position addition can be done.
     */
    public QuadTree(float min, float max) {
        this(new Vector2((min + max) * 0.5f, (min + max) * 0.5f), (min + max) * 0.5f);
    }

    /**
     * Constructs the sub next part of the parent area.
     * @param corner1   One of the corners of the square part.
     * @param corner2   Other corner of the square part.
     * @param nextPart  Next part of this part.
     */
    private QuadTree(Vector2 corner1, Vector2 corner2, QuadTree nextPart) {
        boundary = new RectangleBoundary(corner1, corner2);
        this.nextPart = nextPart;
    }

    /**
     * Constructs a square quadtree with given center and half of its side length.
     * @param center            Center position of this quadtree.
     * @param halfSideLength    Half of its side length of this quadtree.
     */
    private QuadTree(Vector2 center, float halfSideLength) {
        boundary =  new RectangleBoundary(center, Vector2.sum(center, new Vector2(halfSideLength,  halfSideLength)));
        nextPart =  new QuadTree(center, Vector2.sum(center, new Vector2(-halfSideLength,  halfSideLength)),
                    new QuadTree(center, Vector2.sum(center, new Vector2(-halfSideLength, -halfSideLength)),
                    new QuadTree(center, Vector2.sum(center, new Vector2( halfSideLength, -halfSideLength)),
            null)));
    }

    /**
     * Adds given position to quadtree.
     * @param x Horizontal value of the position.
     * @param y Vertical value of th position
     * @return  <code>true</code> if position was added successfully, <code>false</code> otherwise.
     */
    public boolean add(float x, float y) {
        return add(new Vector2(x, y));
    }

    /**
     * Adds given position to quadtree.
     * @param point Position.
     * @return      <code>true</code> if position was added successfully, <code>false</code> otherwise.
     */
    public boolean add(Vector2 point) {
        System.out.println(boundary + ", " + point);
        if(boundary.isInside(point)){
            if(value == null){
                value = point;
                return true;
            }
            else if(subArea == null){
                subArea = new QuadTree(Vector2.sum(boundary.vertexAt(0), boundary.vertexAt(1)).scale(0.5f), Math.abs(boundary.vertexAt(0).getX() - boundary.vertexAt(1).getX()) * 0.5f);
                subArea.add(value);
                return subArea.add(point);
            }
            else{
                return subArea.add(point);
            }
        }
        else{
            if(nextPart == null) return false;
            return nextPart.add(point);
        }
    }

    /**
     * Generates the <code>String</code> representation of this quadtree.
     * @return  <code>String</code> representation of this quadtree.
     */
    @Override
    public String toString() {
        return toString(0);
    }

    /**
     * Generates the <code>String</code> representation of this quadtree.
     * @return  <code>String</code> representation of this quadtree.
     */
    private String toString(int depth) {
        StringBuilder result = new StringBuilder();

        result.append("\t".repeat(depth)).append(boundary).append("\n");

        if(subArea == null)     result.append("\t".repeat(depth + 1)).append(value).append("\n");
        else                    result.append(subArea.toString(depth + 1));

        if(nextPart != null)    result.append(nextPart.toString(depth));

        return result.toString();
    }
}
