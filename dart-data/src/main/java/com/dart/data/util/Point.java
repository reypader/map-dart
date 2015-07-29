package com.dart.data.util;

/**
 * Convenience class to represent a point.
 * <p>
 * Provides alternate methods to access the X and Y fields as
 * Longitude and Latitude respectively
 * <p>
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
}
