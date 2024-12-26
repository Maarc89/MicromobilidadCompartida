package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.VehicleID;
import exceptions.*;
import services.Server;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.time.Duration;
import java.time.LocalDateTime;

import java.awt.image.BufferedImage;
import java.time.LocalTime;

public class JourneyRealizeHandler {


    private GeographicPoint originalPoint;
    private QRDecoder qrdecoder;
    private UnbondedBTSignal bluetooth;
    private BufferedImage img;
    private Server server;
    private ArduinoMicroController arduino;

    private StationID orgStatId; //origin station id
    private PMVehicle pmVehicle; // primary mobility vehicle

    private float importe;

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
        // s'inicialitzen els dos
        s.setServiceInit();
        s.setOriginPoint();


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
    public void broadcastStationID(String stID) throws ConnectException {
        try {
            arduino.setBTconnection();
        } catch (Exception e) {
            throw new ConnectException("Error al establecer la conexión Bluetooth: " + e.getMessage());
        }

        try {
            bluetooth.BTbroadcast();
        } catch (Exception e) {
            throw new ConnectException("Error durante la transmisión del ID por Bluetooth: " + e.getMessage());
        }

        System.out.println("StationID transmitido: " + stID);

    }



    // Input events from the Arduino microcontroller channel
    public void startDriving() throws ConnectException, ProceduralException {

        try {
            bluetooth.BTbroadcast();
        } catch (Exception e) {
            throw new ConnectException("Error durante la transmisión del ID por Bluetooth: " + e.getMessage());
        }

        arduino.startDriving();

        server.registerPairing();
        pmVehicle.setNotAvailb();

        try {
            JourneyService s = new JourneyService();
            pmVehicle.setUnderWay();
            s.setInProgress(true);
        } catch (Exception e) {
            throw new ProceduralException("ERROR");
        }


    }

    public void stopDriving() throws ConnectException, ProceduralException, PMVPhysicalException {
        try {
            bluetooth.BTbroadcast();
        } catch (Exception e) {
            throw new ConnectException("Error durante la transmisión del ID por Bluetooth: " + e.getMessage());
        }

        arduino.stopDriving();

    }

    // Internal operations
    private void calculateValues(GeographicPoint gP, LocalDateTime date) {
        // duración, distancia y velocidad promedio.

        if (pmVehicle == null) {
            throw new IllegalArgumentException("Vehicle no disponible");
        }
        //distancia
        GeographicPoint vehicleLocation = pmVehicle.getLocation();
        float distance = vehicleLocation.calculateDistance(gP);
        //duracio
        JourneyService s = new JourneyService();
        LocalTime initTime = s.getInitHour();
        if (initTime == null) {
            throw new IllegalStateException("El servicio no se ha inicializado correctamente.");
        }
        s.setEndHour(date.toLocalTime());
        LocalTime endTime = date.toLocalTime();
        //paquete time de java
        Duration duration = Duration.between(initTime, endTime);
        float minutes = duration.toMinutes();
        //velocitat mitja
        float avgSpeed = distance/minutes*60;

    }


    private void calculateImport(float distance, int duration, float averageSpeed, LocalDateTime date) {
        float costoKm = 0.5f;
        float costoMinuto = 0.2f;

        //(distancia * costxKM) + (duracio * costXMinut)
        importe = (distance * costoKm + duration * costoMinuto);

        // arrodonim a 2 decimals perls centims
        importe = Math.round(importe * 100) / 100f;
    }

    public float getImporte() {
        return importe;
    }

    // Setter methods for injecting dependencies
}