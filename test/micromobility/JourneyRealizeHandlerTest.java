/*package micromobility;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import exceptions.*;
import micromobility.JourneyRealizeHandler;
import micromobility.PMVehicle;
import micromobility.PMVehicle.PMVState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import services.Server;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class JourneyRealizeHandlerTest {

    private QRDecoder qrDecoderMock;
    private Server serverMock;
    private UnbondedBTSignal bluetoothMock;
    private ArduinoMicroController arduinoMock;
    private PMVehicle pmVehicleMock;
    private BufferedImage qrImageMock;
    private StationID originStationMock;
    private GeographicPoint originLocationMock;

    private JourneyRealizeHandler handler;

    @BeforeEach
    void setUp() {
        qrDecoderMock = mock(QRDecoder.class);
        serverMock = mock(Server.class);
        bluetoothMock = mock(UnbondedBTSignal.class);
        arduinoMock = mock(ArduinoMicroController.class);
        pmVehicleMock = mock(PMVehicle.class);
        qrImageMock = mock(BufferedImage.class);
        originLocationMock = mock(GeographicPoint.class);

        originStationMock = new StationID("ST1234");

        handler = new JourneyRealizeHandler(
                qrDecoderMock,
                originStationMock,
                pmVehicleMock,
                arduinoMock,
                serverMock,
                qrImageMock,
                bluetoothMock
        );
    }

    @Test
    void testScanQR_SuccessfulPairing() throws Exception {
        // Mock inputs and expected interactions
        VehicleID vehicleID = new VehicleID("VH12345");
        LocalDateTime now = LocalDateTime.now();

        when(qrDecoderMock.getVehicleID(qrImageMock)).thenReturn(vehicleID);
        doNothing().when(serverMock).checkPMVAvail(vehicleID);
        doNothing().when(serverMock).registerPairing(
                any(UserAccount.class), eq(vehicleID), eq(originStationMock), eq(originLocationMock), any(LocalDateTime.class)
        );
        doNothing().when(serverMock).setPairing(
                any(UserAccount.class), eq(vehicleID), eq(originStationMock), eq(originLocationMock), any(LocalDateTime.class)
        );
        doNothing().when(arduinoMock).setBTconnection();
        doNothing().when(pmVehicleMock).setNotAvailb();

        // Execute
        handler.scanQR();

        // Verify interactions
        verify(qrDecoderMock).getVehicleID(qrImageMock);
        verify(serverMock).checkPMVAvail(vehicleID);
        verify(serverMock).registerPairing(
                any(UserAccount.class), eq(vehicleID), eq(originStationMock), eq(originLocationMock), any(LocalDateTime.class)
        );
        verify(serverMock).setPairing(
                any(UserAccount.class), eq(vehicleID), eq(originStationMock), eq(originLocationMock), any(LocalDateTime.class)
        );
        verify(arduinoMock).setBTconnection();
        verify(pmVehicleMock).setNotAvailb();
    }

    @Test
    void testScanQR_ThrowsPMVNotAvailException() throws Exception {
        // Mock a PMVNotAvailException
        when(qrDecoderMock.getVehicleID(qrImageMock)).thenReturn(new VehicleID("VH12345"));
        doThrow(new PMVNotAvailException("PMV not available"))
                .when(serverMock).checkPMVAvail(any(VehicleID.class));

        // Execute and verify
        PMVNotAvailException exception = assertThrows(PMVNotAvailException.class, handler::scanQR);
        assertEquals("PMV not available", exception.getMessage());

        // Ensure no further interactions occurred after the exception
        verify(serverMock, never()).registerPairing(
                any(UserAccount.class), any(VehicleID.class), any(StationID.class), any(GeographicPoint.class), any(LocalDateTime.class)
        );
        verify(arduinoMock, never()).setBTconnection();
    }

    @Test
    void testStartDriving() throws Exception {
        // Mock setup
        doNothing().when(pmVehicleMock).setUnderWay();

        // Execute
        handler.startDriving();

        // Verify interactions
        verify(pmVehicleMock).setUnderWay();
    }

    @Test
    void testUnPairVehicle_CalculatesValuesAndUnPairs() throws Exception {
        // Mock setup
        GeographicPoint endPoint = mock(GeographicPoint.class);
        when(pmVehicleMock.getLocation()).thenReturn(endPoint);

        LocalDateTime now = LocalDateTime.now();
        when(originLocationMock.calculateDistance(endPoint)).thenReturn(5.0f);

        // Execute
        handler.unPairVehicle();

        // Verify calculations
        verify(pmVehicleMock).getLocation();
        verify(originLocationMock).calculateDistance(endPoint);

        // Verify server interactions
        verify(serverMock).stopPairing(
                any(UserAccount.class), any(VehicleID.class), any(StationID.class), eq(endPoint), eq(now),
                anyFloat(), anyFloat(), anyInt(), any(BigDecimal.class)
        );
    }
}*/
