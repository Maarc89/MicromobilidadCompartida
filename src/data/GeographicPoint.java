package data;

import exceptions.InvalidLongLattException;

/**
 * Essential data classes
 */

final public class GeographicPoint {
    // The geographical coordinates expressed as decimal degrees
    private final float latitude;
    private final float longitude;

    public GeographicPoint(float latitude, float longitude) {

        if (latitude < -90.0f || latitude > 90.0f) {
            throw new InvalidLongLattException("Latitude must be between -90.0 and 90.0 degrees.");
        }
        if (longitude < -180.0f || longitude > 180.0f) {
            throw new IllegalArgumentException("Longitude must be between -180.0 and 180.0 degrees.");
        }

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        boolean eq;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeographicPoint gP = (GeographicPoint) o;
        eq = ((latitude == gP.latitude) && (longitude == gP.longitude));
        return eq;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(latitude);
        result = prime * result + Float.floatToIntBits(longitude);
        return result;
    }

    @Override
    public String toString() {
        return "GeographicPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
