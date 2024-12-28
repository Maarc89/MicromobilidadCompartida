package smartfeatures;

import exceptions.ConnectException;
import exceptions.PMVPhysicalException;
import exceptions.ProceduralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.smartfeatures.ArduinoMicroController;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestDoblesArduinoMicroController {
    private ArduinoMicroController mockController;

    @BeforeEach
    void setUp() {
        mockController = Mockito.mock(ArduinoMicroController.class);
    }

    @Test
    void testSetBTConnectionSuccess() throws ConnectException {
        doNothing().when(mockController).setBTconnection();
        mockController.setBTconnection();
        verify(mockController, times(1)).setBTconnection();
    }

    @Test
    void testSetBTConnectionThrowsException() throws ConnectException {
        doThrow(new ConnectException("Bluetooth connection failed"))
                .when(mockController).setBTconnection();
        assertThrows(ConnectException.class, mockController::setBTconnection);
    }

    @Test
    void testStartDrivingSuccess() throws PMVPhysicalException, ConnectException, ProceduralException {
        doNothing().when(mockController).startDriving();
        mockController.startDriving();
        verify(mockController, times(1)).startDriving();
    }

    @Test
    void testStartDrivingThrowsPMVPhysicalException() throws PMVPhysicalException, ConnectException, ProceduralException {
        doThrow(new PMVPhysicalException("Physical error detected"))
                .when(mockController).startDriving();
        assertThrows(PMVPhysicalException.class, mockController::startDriving);
    }

    @Test
    void testStartDrivingThrowsConnectException() throws PMVPhysicalException, ConnectException, ProceduralException {
        doThrow(new ConnectException("Connection lost"))
                .when(mockController).startDriving();
        assertThrows(ConnectException.class, mockController::startDriving);
    }

    @Test
    void testStartDrivingThrowsProceduralException() throws PMVPhysicalException, ConnectException, ProceduralException {
        doThrow(new ProceduralException("Procedure violation"))
                .when(mockController).startDriving();
        assertThrows(ProceduralException.class, mockController::startDriving);
    }

    @Test
    void testUndoBTConnectionSuccess() {
        doNothing().when(mockController).undoBTconnection();
        mockController.undoBTconnection();
        verify(mockController, times(1)).undoBTconnection();
    }
}
