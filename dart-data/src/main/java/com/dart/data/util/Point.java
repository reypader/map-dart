package com.dart.data.util;

import java.util.Objects;

/**
 * Convenience class to represent a point.
 * <p/>
 * Provides alternate methods to access the X and Y fields as
 * Longitude and Latitude respectively
 * <p/>
 *
 * @author RMPader
 */
public class Point {
    private final float x;
    private final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLongitude() {
        return x;
    }

    public float getLatitude() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(getX(), point.getX()) &&
                Objects.equals(getY(), point.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
