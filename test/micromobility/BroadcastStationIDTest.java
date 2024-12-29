package micromobility;

import data.StationID;
import exceptions.ConnectException;
import org.junit.jupiter.api.Test;
import services.smartfeatures.MockUnbondedBTSignal;
import services.smartfeatures.UnbondedBTSignal;

import static org.junit.jupiter.api.Assertions.*;

public class BroadcastStationIDTest {

    @Test
    public void testBroadcastStationID_Success() {
        JourneyRealizeHandler journeyHandler = new JourneyRealizeHandler();
        MockUnbondedBTSignal mockBluetooth = new MockUnbondedBTSignal(journeyHandler);
        journeyHandler.setBluetooth(mockBluetooth);
        StationID stationID = new StationID("ST1234");
        assertDoesNotThrow(() -> journeyHandler.broadcastStationID(stationID));

        System.out.println("Test de transmisión exitosa completado.");
    }

    @Test
    public void testBroadcastStationID_ThrowsConnectException() {
        JourneyRealizeHandler journeyHandler = new JourneyRealizeHandler();
        MockUnbondedBTSignal mockBluetooth = new MockUnbondedBTSignal(journeyHandler);
        journeyHandler.setBluetooth(mockBluetooth);
        StationID stationID = new StationID("ST1234");
        ConnectException exception = assertThrows(ConnectException.class, () -> {
            journeyHandler.broadcastStationID(stationID);
        });

        assertEquals("Simulated connection failure", exception.getMessage());
        System.out.println("Test de transmisión con fallo completado.");
    }
}
