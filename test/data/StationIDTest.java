package data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StationIDTest {

    @Test
    public void testValidStationID() {
        StationID stationID = new StationID("ST1234");
        assertEquals("ST1234", stationID.getId());
    }

    @Test
    public void testNullStationID() {
        assertThrows(NullPointerException.class, () -> new StationID(null));
    }

    @Test
    public void testMalformedStationID() {
        assertThrows(IllegalArgumentException.class, () -> new StationID("123456"));
        assertThrows(IllegalArgumentException.class, () -> new StationID("st1234"));
        assertThrows(IllegalArgumentException.class, () -> new StationID("ST12"));
    }

    @Test
    public void testEqualityAndHashCode() {
        StationID id1 = new StationID("ST1234");
        StationID id2 = new StationID("ST1234");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void testToString() {
        StationID stationID = new StationID("ST1234");
        assertEquals("StationID{id='ST1234'}", stationID.toString());
    }
}
