package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class JourneyServiceTest {

    private JourneyService journeyService;

    @BeforeEach
    void setUp() {
        UserAccount user = new UserAccount("USR123", "Maria Abc", "Abmaria1", "maria.abc@example.com"); // Mock user
        VehicleID vehicle = new VehicleID("VH12345"); // Mock vehicle
        StationID startStation = new StationID("ST1234");
        GeographicPoint originPoint = new GeographicPoint(40.2f, -74.7f);

        journeyService = new JourneyService(user, vehicle, LocalDate.now(), LocalTime.of(10, 0), originPoint, startStation);
    }

    @Test
    void testSetAndGetStartStation() {
        StationID newStartStation = new StationID("ST1234");
        journeyService.setStartStation(newStartStation);
        assertEquals(newStartStation, journeyService.getStartStation());
    }

    @Test
    void testSetDuration() {
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(10, 30);
        int duration = journeyService.setDuration(start, end);
        assertEquals(30, duration);
    }

    @Test
    void testSetDistance() {
        journeyService.setDistance(5.0f);
        assertEquals(5.0f, journeyService.getDistance());
    }

    @Test
    void testSetAvgSpeed() {
        journeyService.setAvgSpeed(10.0f, 30.0f);
        assertEquals(20.0f, journeyService.getAvgSpeed(), 0.01f);
    }

    @Test
    void testSetServiceInit() {
        journeyService.setServiceInit();
        assertEquals(LocalDate.now(), journeyService.getInitDate());
        assertNotNull(journeyService.getInitHour());
    }

    @Test
    void testSetServiceFinish() {
        LocalTime endTime = LocalTime.of(11, 0);
        journeyService.setServiceFinish(endTime, 10.0f, 60, 10.0f, BigDecimal.valueOf(15.0));

        assertEquals(endTime, journeyService.getEndHour());
        assertEquals(10.0f, journeyService.getDistance());
        assertEquals(60, journeyService.getDuration());
        assertEquals(10.0f, journeyService.getAvgSpeed(), 0.01f);
        assertEquals(BigDecimal.valueOf(15.0), journeyService.getImporte());
    }

    @Test
    void testSetAndGetOriginPoint() {
        GeographicPoint newPoint = new GeographicPoint(41.3f, 2.1f); // Mock point
        journeyService.setOriginPoint(newPoint);
        assertEquals(newPoint, journeyService.getOriginPoint());
    }

    @Test
    void testSetAndGetEndPoint() {
        GeographicPoint endPoint = new GeographicPoint(42.3f, -71.0f); // Mock point
        journeyService.setEndPoint(endPoint);
        assertEquals(endPoint, journeyService.getEndPoint());
    }

    @Test
    void testSetInProgress() {
        journeyService.setInProgress(true);
        assertTrue(journeyService.isInProgress());

        journeyService.setInProgress(false);
        assertFalse(journeyService.isInProgress());
    }
}
