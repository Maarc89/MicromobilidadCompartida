package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.VehicleID;
import exceptions.*;
import services.Server;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.time.LocalDateTime;

import java.awt.image.BufferedImage;

public class JourneyRealizeHandler {


    private QRDecoder qrdecoder;
    private UnbondedBTSignal bluetooth;
    private BufferedImage img;
    private Server server;
    private ArduinoMicroController arduino;

    private StationID orgStatId; //origin station id
    private PMVehicle pmVehicle; // primary mobility vehicle


    public JourneyRealizeHandler(QRDecoder qrdecoder, StationID orgStatId, PMVehicle pmVehicle, ArduinoMicroController arduino, Server server, BufferedImage img, UnbondedBTSignal bluetooth) {
        this.qrdecoder = qrdecoder;
        this.orgStatId = orgStatId;
        this.pmVehicle = pmVehicle;
        this.arduino = arduino;
        this.server = server;
        this.img = img;
        this.bluetooth = bluetooth;
    }


    // User interface input events
    public void scanQR() throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException,
            ProceduralException {


        if (orgStatId == null || !isPMVehicleinZone()) {
            throw new ProceduralException("La estacion o el vehiculo no estan correctamente detectados");
        }


        VehicleID id = qrdecoder.getVehicleID(img);
        server.checkPMVAvail(id);

        JourneyService s = new JourneyService();

        s.setStartTime(LocalDateTime.now());
        s.set

        bluetooth.BTbroadcast();


        System.out.println("Vehicle Pairing Completed");
        System.out.println("You can start driving");
    }


    public boolean isPMVehicleinZone() {

        //GeographicPoint stationLocation = orgStatId.ge
        GeographicPoint vehicleLocation = pmVehicle.getLocation();


        return true;
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