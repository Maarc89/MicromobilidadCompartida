package data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class VehicleIDTest {

    @Test
    public void testValidVehicleID() {
        VehicleID vehicleID = new VehicleID("VH12345");
        assertEquals("VH12345", vehicleID.getId());
    }

    @Test
    public void testNullVehicleID() {
        assertThrows(NullPointerException.class, () -> new VehicleID(null));
    }

    @Test
    public void testMalformedVehicleID() {
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("12345"));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("vh12345")); // Minúsculas
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("VH1234")); // Menos de 5 dígitos
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("VH123456")); // Más de 5 dígitos
    }

    @Test
    public void testEqualityAndHashCode() {
        VehicleID id1 = new VehicleID("VH12345");
        VehicleID id2 = new VehicleID("VH12345");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void testToString() {
        VehicleID vehicleID = new VehicleID("VH12345");
        assertEquals("VehicleID{id='VH12345'}", vehicleID.toString());
    }
}
