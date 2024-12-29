package micromobility.tests;

import exceptions.ConnectException;
import exceptions.PMVPhysicalException;
import exceptions.ProceduralException;
import micromobility.JourneyRealizeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.MockArduinoMicroController;

class StartDrivingTest {

    private JourneyRealizeHandler journeyHandler;
    private MockArduinoMicroController mockArduino;

    @BeforeEach
    void setUp() {
        journeyHandler = new JourneyRealizeHandler();
        mockArduino = new MockArduinoMicroController(false, true); // Initial state: not connected, no physical error
        journeyHandler.setArduino(mockArduino);
    }

    @Test
    void testStartDrivingSuccess() {
        try {
            mockArduino.setBTconnection(); // Simulate successful connection
            mockArduino.startDriving();
            Assertions.assertTrue(true, "Driving started successfully");
        } catch (Exception e) {
            Assertions.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testStartDrivingWithoutConnection() {
        Assertions.assertThrows(ConnectException.class, () -> {
            mockArduino.startDriving();
        }, "Expected ConnectException when starting without connection");
    }

    @Test
    void testStartDrivingWithPhysicalError() {
        try {
            mockArduino.setBTconnection();
            mockArduino.setPhysicalErrorState(true); // Simulate physical error
            Assertions.assertThrows(PMVPhysicalException.class, () -> {
                mockArduino.startDriving();
            }, "Expected PMVPhysicalException due to a physical error");
        } catch (Exception e) {
            Assertions.fail("Unexpected exception during setup: " + e.getMessage());
        }
    }

    @Test
    void testStartDrivingWithProceduralException() {
        Assertions.assertThrows(ProceduralException.class, () -> {
            journeyHandler.startDriving();
        }, "Expected ProceduralException when starting driving in an invalid state");
    }
}