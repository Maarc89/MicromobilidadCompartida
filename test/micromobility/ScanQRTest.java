package micromobility;

import data.*;
import exceptions.*;
import micromobility.JourneyRealizeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.MockServer;
import services.smartfeatures.MockQRDecoder;
import services.smartfeatures.MockArduinoMicroController;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScanQRTest {

    private JourneyRealizeHandler journeyHandler;
    private MockQRDecoder mockQRDecoder;
    private MockServer mockServer;
    private MockArduinoMicroController mockArduino;
    private GeographicPoint mockGeographic;

    private UserAccount mockUser;
    private VehicleID mockVehicleID;
    private StationID mockStationID;
    private BufferedImage mockImage;

    @BeforeEach
    void setUp() throws ConnectException {
        journeyHandler = new JourneyRealizeHandler();
        JourneyService j = new JourneyService();
        journeyHandler.setJourneyService(j);
        mockVehicleID = new VehicleID("VH12345");
        mockQRDecoder = new MockQRDecoder(mockVehicleID, true);
        mockServer = new MockServer(true);
        mockArduino = new MockArduinoMicroController(false, true);
        mockGeographic = new GeographicPoint(1.0F, 1.0F);

        mockUser = new UserAccount("VHF333", "Jordi", "1Ajijiji", "test@example.com");
        mockStationID = new StationID("ST0012");

        journeyHandler.setQrdecoder(mockQRDecoder);
        journeyHandler.setServer(mockServer);
        journeyHandler.setArduino(mockArduino);
        journeyHandler.setStation(mockStationID, true);

        journeyHandler.broadcastStationID(mockStationID);
    }

    @Test
    void testScanQRSuccess() throws ConnectException, InvalidPairingArgsException, CorruptedImgException, PMVNotAvailException, ProceduralException {
        mockServer.setVehicleAvailability(mockVehicleID, true);
        journeyHandler.scanQR();

        assertNotNull(journeyHandler);
        assertEquals(mockVehicleID, mockQRDecoder.getVehicleID(mockImage));
    }

    @Test
    void testScanQRVehicleNotAvailable() {
        mockServer.setVehicleAvailability(mockVehicleID, false);

        assertThrows(PMVNotAvailException.class, () -> {
            journeyHandler.scanQR();
        });
    }

    @Test
    void testScanQRInvalidImage() {
        MockQRDecoder invalidQRDecoder = new MockQRDecoder(mockVehicleID, false);
        journeyHandler.setQrdecoder(invalidQRDecoder);

        assertThrows(CorruptedImgException.class, () -> {
            journeyHandler.scanQR();
        });
    }

    @Test
    void testScanQRWithNoStationSet() {

        journeyHandler.originStationId = null;
        assertThrows(ProceduralException.class, () -> {
            journeyHandler.scanQR();
        });
    }


    @Test
    void testScanQRWithDisconnectedServer() {
        mockServer.setServerConnected(false);

        assertThrows(ConnectException.class, () -> {
            journeyHandler.scanQR();
        });
    }

    @Test
    void testScanQRWithVehicleInvalidImage() {
        MockQRDecoder invalidQRDecoder = new MockQRDecoder(mockVehicleID, false);
        journeyHandler.setQrdecoder(invalidQRDecoder);

        assertThrows(CorruptedImgException.class, () -> {
            journeyHandler.scanQR();
        });
    }

    @Test
    void testScanQRSuccessWithDifferentStation() throws ConnectException, InvalidPairingArgsException, CorruptedImgException, PMVNotAvailException, ProceduralException {
        StationID newStationID = new StationID("ST0012");
        journeyHandler.setStation(newStationID, true);

        mockServer.setVehicleAvailability(mockVehicleID, true);
        journeyHandler.scanQR();


        assertEquals(mockVehicleID, mockQRDecoder.getVehicleID(mockImage));
    }
}
