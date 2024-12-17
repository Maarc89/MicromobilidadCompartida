package data;

import exceptions.InvalidLongLattException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeographicPointTest {



    @Test
    void validCoordinates() {
        GeographicPoint point = new GeographicPoint(40.7128f, -74.0060f);
        assertEquals(40.7128f, point.getLatitude());
        assertEquals(-74.0060f, point.getLongitude());
    }

    @Test
    void invalidLatitudeThrowsException() {
        Exception exception = assertThrows(InvalidLongLattException.class, () -> {
            new GeographicPoint(100.0f, -74.0060f);
        });
        assertEquals("Latitude must be between -90.0 and 90.0 degrees.", exception.getMessage());
    }

    @Test
    void invalidLongitudeThrowsException() {
        Exception exception = assertThrows(InvalidLongLattException.class, () -> {
            new GeographicPoint(40.7128f, -200.0f);
        });
        assertEquals("Longitude must be between -180.0 and 180.0 degrees.", exception.getMessage());
    }

    @Test
    void equalityAndHashcode() {
        GeographicPoint point1 = new GeographicPoint(40.7128f, -74.0060f);
        GeographicPoint point2 = new GeographicPoint(40.7128f, -74.0060f);
        assertEquals(point1, point2);
        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    void toStringTest() {
        GeographicPoint point = new GeographicPoint(40.7128f, -74.0060f);
        assertEquals("GeographicPoint{latitude=40.7128, longitude=-74.006}", point.toString());
    }
}
