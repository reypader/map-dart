package com.dart.data.util;

/**
 * Convenience class to represent a rectangle using its
 * north-east and south-west corner.
 *
 * @author RMPader
 */
public class Rectangle {
    private final Point northEastCorner;
    private final Point southWestCorner;

    public Rectangle(Point northEastCorner, Point southWestCorner) {
        this.northEastCorner = northEastCorner;
        this.southWestCorner = southWestCorner;
    }

    public Point getNorthEastCorner() {
        return northEastCorner;
    }

    public Point getSouthWestCorner() {
        return southWestCorner;
    }
}
