package micromobility;

import data.GeographicPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PMVehicleTest {
    private PMVehicle pmVehicle;
    private GeographicPoint initialLocation;

    @BeforeEach
    void setUp() {
        initialLocation = new GeographicPoint(10.0F, 20.0F);
        pmVehicle = new PMVehicle(PMVehicle.PMVState.Available, initialLocation);
    }

    @Test
    void testInitialStateAndLocation() {
        assertEquals(PMVehicle.PMVState.Available, pmVehicle.getState());
        assertEquals(initialLocation, pmVehicle.getLocation());
    }

    @Test
    void testSetNotAvailable() {
        pmVehicle.setNotAvailb();
        assertEquals(PMVehicle.PMVState.NotAvailable, pmVehicle.getState());
    }

    @Test
    void testSetUnderWay() {
        pmVehicle.setUnderWay();
        assertEquals(PMVehicle.PMVState.UnderWay, pmVehicle.getState());
    }

    @Test
    void testSetAvailable() {
        pmVehicle.setAvailb();
        assertEquals(PMVehicle.PMVState.Available, pmVehicle.getState());
    }

    @Test
    void testSetLocation() {
        GeographicPoint newLocation = new GeographicPoint(30.0F, 40.0F);
        pmVehicle.setLocation(newLocation);
        assertEquals(newLocation, pmVehicle.getLocation());
    }

    @Test
    void testTemporaryParkingState() {
        pmVehicle.setUnderWay(); // change to a state before testing state consistency
        assertNotEquals(PMVehicle.PMVState.TemporaryParking, pmVehicle.getState());
    }
}
