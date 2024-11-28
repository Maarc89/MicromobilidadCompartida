package micromobility;

public class JourneyRealizeHandler {
    // ???

    // The class members

    // The constructor/s

    // Different input events that intervene

    // User interface input events
    public void scanQR() throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException,
            ProceduralException {
    }

    public void unPairVehicle() throws ConnectException, InvalidPairingArgsException,
            PairingNotFoundException, ProceduralException {
    }

    // Input events from the unbonded Bluetooth channel
    public void broadcastStationID(StationID stID) throws ConnectException {
    }

    // Input events from the Arduino microcontroller channel
    public void startDriving() throws ConnectException, ProceduralException {
    }

    public void stopDriving() throws ConnectException, ProceduralException {
    }

    // Internal operations
    private void calculateValues(GeographicPoint gP, LocalDateTime date) {
    }

    private void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {
    }

    // Setter methods for injecting dependencies
}
