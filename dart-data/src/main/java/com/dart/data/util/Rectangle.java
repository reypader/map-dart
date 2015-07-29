package com.dart.data.util;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Objects.equals(getNorthEastCorner(), rectangle.getNorthEastCorner()) &&
                Objects.equals(getSouthWestCorner(), rectangle.getSouthWestCorner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNorthEastCorner(), getSouthWestCorner());
    }
}
