package micromobility;

import data.GeographicPoint;
import data.StationID;
import exceptions.*;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.time.LocalDateTime;

import java.awt.image.BufferedImage;

public class JourneyRealizeHandler {
    // ???

    private QRDecoder qrdecoder;
    private UnbondedBTSignal bluetooth;
    private BufferedImage img;
    // The class members
    /*StationID stID;
    GeographicPoint gP;
    LocalDateTime date;
    float distance;
    int duration;
    float averageSpeed;
*/


    // The constructor/s
    public

    // Different input events that intervene

    // User interface input events
    public void scanQR() throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException,
            ProceduralException {



            qrdecoder.getVehicleID(img);
            bluetooth.BTbroadcast();

            System.out.println();
            System.out.println();
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

    private void calculateImport(float distance, int duration, float averageSpeed, LocalDateTime date) {

    }

    // Setter methods for injecting dependencies
}