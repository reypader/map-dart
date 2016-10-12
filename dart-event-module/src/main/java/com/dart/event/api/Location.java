package com.dart.event.api;

import java.util.Objects;

/**
 * Created by RMPader on 7/28/15.
 */
public class Location {

    private float longitude;

    private float latitude;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return (Float.compare(getLongitude(), location.getLongitude()) == 0) &&
                (Float.compare(getLatitude(), location.getLatitude()) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLongitude(), getLatitude());
    }
}
