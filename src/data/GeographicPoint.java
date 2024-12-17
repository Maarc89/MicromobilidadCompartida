package data;

/**
 * Essential data classes
 */

final public class GeographicPoint {
    // The geographical coordinates expressed as decimal degrees
    private final float latitude;
    private final float longitude;

    public GeographicPoint(float latitude, float longitude) {
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

        System.out.println("winwin");
        System.out.println("aaaa");
        System.out.println("aaaaaa");

        System.out.println("h");

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
