package services;

import data.*;
import exceptions.*;
import micromobility.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MockServerTest {

    private MockServer mockServer;
    private UserAccount user;
    private VehicleID vehicle;
    private StationID station;
    private GeographicPoint location;
    private LocalDateTime dateTime;

    @BeforeEach
    void setUp() {
        mockServer = new MockServer(true); // Server is available by default
        user = new UserAccount("USR123", "John Doe", "Password1!", "john@example.com");
        vehicle = new VehicleID("VH12345");
        station = new StationID("ST1234");
        location = new GeographicPoint(40.2f, -74.7f);
        dateTime = LocalDateTime.now();
    }

    @Test
    void checkPMVAvail_AvailableVehicle() throws PMVNotAvailException, ConnectException {
        mockServer.setVehicleAvailability(vehicle, true);
        assertDoesNotThrow(() -> mockServer.checkPMVAvail(vehicle));
    }

    @Test
    void checkPMVAvail_UnavailableVehicle() {
        mockServer.setVehicleAvailability(vehicle, false);
        PMVNotAvailException exception = assertThrows(PMVNotAvailException.class, () -> mockServer.checkPMVAvail(vehicle));
        assertEquals("PMV not available: VH12345", exception.getMessage());
    }

    @Test
    void checkPMVAvail_ServerUnavailable() {
        mockServer.setAvailable(false);
        ConnectException exception = assertThrows(ConnectException.class, () -> mockServer.checkPMVAvail(vehicle));
        assertEquals("Server is unavailable.", exception.getMessage());
    }

    @Test
    void registerPairing_ValidPairing() throws InvalidPairingArgsException, ConnectException {
        mockServer.registerPairing(user, vehicle, station, location, dateTime);
        // No exception means the operation was successful
    }

    @Test
    void registerPairing_UserAlreadyPaired() throws InvalidPairingArgsException, ConnectException {
        mockServer.registerPairing(user, vehicle, station, location, dateTime);
        InvalidPairingArgsException exception = assertThrows(InvalidPairingArgsException.class, () ->
                mockServer.registerPairing(user, vehicle, station, location, dateTime)
        );
        assertEquals("User already has an active pairing.", exception.getMessage());
    }

    @Test
    void stopPairing_ValidPairing() throws InvalidPairingArgsException, ConnectException {
        mockServer.registerPairing(user, vehicle, station, location, dateTime);
        mockServer.stopPairing(user, vehicle, station, location, dateTime, 20.5f, 10.0f, 15, BigDecimal.valueOf(5.0));
        // No exception means the operation was successful
    }

    @Test
    void stopPairing_NoActivePairing() {
        InvalidPairingArgsException exception = assertThrows(InvalidPairingArgsException.class, () ->
                mockServer.stopPairing(user, vehicle, station, location, dateTime, 20.5f, 10.0f, 15, BigDecimal.valueOf(5.0))
        );
        assertEquals("No active pairing found for user: USR123", exception.getMessage());
    }

    @Test
    void registerPayment_SuccessfulPayment() {
        assertDoesNotThrow(() -> mockServer.registerPayment(new ServiceID("SRV123"), user, BigDecimal.valueOf(10.0), 'C'));
    }

    @Test
    void registerPayment_ServerUnavailable() {
        mockServer.setAvailable(false);
        ConnectException exception = assertThrows(ConnectException.class, () ->
                mockServer.registerPayment(new ServiceID("SRV123"), user, BigDecimal.valueOf(10.0), 'C')
        );
        assertEquals("Server is unavailable.", exception.getMessage());
    }

    @Test
    void unPairRegisterService_ValidUnpairing() throws PairingNotFoundException, InvalidPairingArgsException, ConnectException {
        mockServer.registerPairing(user, vehicle, station, location, dateTime);
        JourneyService service = new JourneyService(user, vehicle, dateTime.toLocalDate(), dateTime.toLocalTime(), location, station);
        assertDoesNotThrow(() -> mockServer.unPairRegisterService(service));
    }

    @Test
    void unPairRegisterService_NoPairingFound() {
        JourneyService service = new JourneyService(user, vehicle, dateTime.toLocalDate(), dateTime.toLocalTime(), location, station);
        PairingNotFoundException exception = assertThrows(PairingNotFoundException.class, () ->
                mockServer.unPairRegisterService(service)
        );
        assertEquals("No pairing found for user: USR123", exception.getMessage());
    }

    @Test
    void registerLocation_Success() {
        assertDoesNotThrow(() -> mockServer.registerLocation(vehicle, station));
    }
}
