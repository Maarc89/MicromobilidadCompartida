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
import java.time.LocalDate;
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
    private float duration;
    private float distance;
    private float avgSpeed;


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

        JourneyService s = new JourneyService();
        // Verificar las precondiciones
        if (pmVehicle == null || !pmVehicle.isUnderWay()) {
            throw new ProceduralException("El vehículo no está en estado UnderWay o no está definido.");
        }
        if (!s.inProgress()) {
            throw new ProceduralException("El trayecto no está en progreso.");
        }


        // Actualizar atributos en JourneyService
        s.setEndPoint(pmVehicle.getLocation()); // Suponiendo que se obtiene la ubicación actual
        s.setEndDate(LocalDate.now());
        s.setEndHour(LocalTime.now());
        // calcular y modificar duración, distancia y velocidad promedio
        calculateValues(pmVehicle.getLocation(),LocalDateTime.now());
        // calcular y modificar el importe
        calculateImport(s.getDistance(), s.getDuration(), s.getAvgSpeed(), LocalDateTime.now());
        s.setImporte(getImporte());
        // METODO PARA PAGO










        s.calculateDuration();
        s.setDistance(pmVehicle.calculateDistance(s.getoriginPoint(), s.getEndPoint())); // Método hipotético
        s.calculateAvgSpeed();

        // Calcular el importe
        float distance = s.getDistance();
        int duration = s.getDuration();
        float averageSpeed = s.getAvgSpeed();
        LocalDateTime date = LocalDateTime.now(); // Puedes usar un valor más preciso si lo tienes
        calculateImport(distance, duration, averageSpeed, date);

        // Actualizar el estado del vehículo
        pmVehicle.setAvailable(true);
        pmVehicle.updateLocation(s.getEndPoint()); // Método para actualizar ubicación

        // Modificar inProgress
        s.setInProgress(false);

        // Mostrar confirmación
        System.out.println("Vehículo desemparejado correctamente");
        System.out.println("Importe total: " + s.getAmount());
        System.out.println("Escoger método de pago");

        // Bloquear vehículo (simulando luces verdes o alertas)
        pmVehicle.lock();
        System.out.println("El vehículo ha sido bloqueado. Luz verde activada.");
        System.out.println("Vehiculo desemparejado correctamente");
        System.out.println("Importe total: " + getImporte());
        System.out.println("Escoger metodo de pago");
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
        JourneyService s = new JourneyService();
        if (pmVehicle == null) {
            throw new IllegalArgumentException("Vehicle no disponible");
        }
        //distancia
        GeographicPoint vehicleLocation = pmVehicle.getLocation();
        float distance = vehicleLocation.calculateDistance(gP);
        s.setDistance(distance);

        //duracio
        LocalTime initTime = s.getInitHour();
        s.setEndHour(date.toLocalTime());
        LocalTime endTime = date.toLocalTime();
        int minutes = s.setDuration(initTime,endTime);

        //velocitat mitja
        s.setAvgSpeed(distance, minutes);
    }


    private void calculateImport(float distance, int duration, float averageSpeed, LocalDateTime date) {
        float costoKm = 0.5f;
        float costoMinuto = 0.2f;

        //(distancia * costxKM) + (duracio * costXMinut)
        this.importe = (distance * costoKm + duration * costoMinuto);

        // arrodonim a 2 decimals perls centims
        this.importe = Math.round(importe * 100) / 100f;

    }

    public float getImporte() {
        return this.importe;
    }

// Setter methods for injecting dependencies
}