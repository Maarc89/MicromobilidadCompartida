package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JourneyServiceTest {

    private JourneyService journeyService;
    private UserAccount user;
    private VehicleID vehicle;
    private StationID station;
    private GeographicPoint location;

    @BeforeEach
    void setUp() {
        user = new UserAccount("ABC123", "Abcde1");
        vehicle = new VehicleID("VH45678");
        station = new StationID("ST8901");
        location = new GeographicPoint(10.0F, 20.0F);

        journeyService = new JourneyService(1, user, vehicle, station, location);
    }

    @Test
    void testInitialization() {
        assertEquals(1, journeyService.getServiceID());
        assertEquals(user, journeyService.getUser());
        assertEquals(vehicle, journeyService.getVehicle());
        assertEquals(station, journeyService.getStartStation());
        assertEquals(location, journeyService.getStartLocation());
        assertNull(journeyService.getStartTime());
        assertNull(journeyService.getEndTime());
        assertEquals(0, journeyService.getDistance());
        assertEquals(0, journeyService.getDuration());
        assertEquals(0, journeyService.getAvgSpeed());
        assertNull(journeyService.getAmount());
        assertFalse(journeyService.isInProgress());
    }

    @Test
    void testSetServiceInit() {
        LocalDateTime startTime = LocalDateTime.now();
        journeyService.setServiceInit(startTime);
        assertEquals(startTime, journeyService.getStartTime());
    }

    @Test
    void testSetServiceFinish() {
        LocalDateTime endTime = LocalDateTime.now();
        journeyService.setServiceFinish(endTime, 15.5f, 30, 31.0f, BigDecimal.valueOf(20.5));

        assertEquals(endTime, journeyService.getEndTime());
        assertEquals(15.5f, journeyService.getDistance());
        assertEquals(30, journeyService.getDuration());
        assertEquals(31.0f, journeyService.getAvgSpeed());
        assertEquals(BigDecimal.valueOf(20.5), journeyService.getAmount());
    }

    @Test
    void testSetInProgress() {
        journeyService.setInProgress(true);
        assertTrue(journeyService.isInProgress());

        journeyService.setInProgress(false);
        assertFalse(journeyService.isInProgress());
    }

    @Test
    void testCalculateDuration() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(45);

        journeyService.setServiceInit(startTime);
        journeyService.setServiceFinish(endTime, 0, 0, 0, null);
        journeyService.calculateDuration();

        assertEquals(45, journeyService.getDuration());
    }

    @Test
    void testCalculateAvgSpeed() {
        journeyService.setServiceFinish(LocalDateTime.now(), 30.0f, 60, 0, null);
        journeyService.calculateAvgSpeed();

        assertEquals(0.5f, journeyService.getAvgSpeed());
    }

    @Test
    void testCalculateAmount() {
        journeyService.setServiceFinish(LocalDateTime.now(), 10.0f, 0, 0, null);
        journeyService.calculateAmount(2.5f);

        assertEquals(BigDecimal.valueOf(25.0), journeyService.getAmount());
    }
}