package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import exceptions.*;
import services.Server;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.awt.image.BufferedImage;
import java.time.LocalTime;

public class JourneyRealizeHandler {

    private static final float COST_PER_KM = 0.5f;
    private static final float COST_PER_MINUTE = 0.2f;

    private UserAccount user;
    private PMVehicle pmVehicle; // primary mobility vehicle
    private StationID originStationId; //origin station id
    private StationID endStationId; //origin station id
    private StationID CurrentStationID; //current station id
    private JourneyService journeyService;


    private QRDecoder qrdecoder;
    private UnbondedBTSignal bluetooth;
    private BufferedImage img;
    private Server server;
    private ArduinoMicroController arduino;


    public JourneyRealizeHandler(QRDecoder qrdecoder, StationID originStationId, PMVehicle pmVehicle, ArduinoMicroController arduino, Server server, BufferedImage img, UnbondedBTSignal bluetooth) {
        this.qrdecoder = qrdecoder;
        this.originStationId = originStationId;
        this.pmVehicle = pmVehicle;
        this.arduino = arduino;
        this.server = server;
        this.img = img;
        this.bluetooth = bluetooth;
    }

    // Input events from the unbonded Bluetooth channel
    public void broadcastStationID(String stID) throws ConnectException {
        bluetooth.BTbroadcast();
        System.out.println("StationID transmitido: " + stID);
        this.CurrentStationID = new StationID(stID);
    }

    // User interface input events
    public void scanQR() throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException,
            ProceduralException {

        VehicleID id = qrdecoder.getVehicleID(img);
        server.checkPMVAvail(id);
        LocalDateTime time = LocalDateTime.now();
        JourneyService s = new JourneyService(null, id, time.toLocalDate(),
                time.toLocalTime(), null, null);
        journeyService = s;
        //s.setServiceInit(); No fem un serviceInit perque ja esta inicialitzat dintre del time/JourneyService
        setStation(CurrentStationID, true);

        arduino.setBTconnection();
        pmVehicle.setNotAvailb();
        server.registerPairing(user, id, originStationId, journeyService.getOriginPoint(), time);
        server.setPairing(user, id, originStationId, journeyService.getOriginPoint(), time);
        System.out.println("Vehicle Pairing Completed");
        System.out.println("You can start driving");
    }


    // Input events from the Arduino microcontroller channel
    public void startDriving() throws ConnectException, ProceduralException {
        pmVehicle.setUnderWay();
        journeyService.setInProgress(true);
        System.out.println("Pantalla de trayecto en curso");
    }

    public void stopDriving() throws ConnectException, ProceduralException {
        System.out.println("Pantalla de vehículo detenido");
    }

    public void unPairVehicle() throws ConnectException, InvalidPairingArgsException,
            PairingNotFoundException, ProceduralException {

        //BROADCAST I PILLAREM LA ENDSTATION
        setStation(CurrentStationID, false);
        journeyService.setEndDate(LocalDate.now()); // em de setejar la data pq no tenim LocalDateTime
        GeographicPoint endPoint = pmVehicle.getLocation();
        LocalDateTime now = LocalDateTime.now();

        // CALCULAR Y SETEJAR distancia, duracio, avgSpeed, import
        calculateValues(endPoint, now);
        calculateImport(journeyService.getDistance(), journeyService.getDuration(),
                journeyService.getAvgSpeed(), LocalDateTime.now()); // li paso aquests dos parametres perquè AL ENUNCIAT
        // funció esta inicialitzada pero no els utlitzo per a res

        // METODO PARA PAGO

        //serviceID
        int numeroAleatorio = (int) (Math.random() * 100) + 1;
        journeyService.setServiceID(numeroAleatorio);

        server.stopPairing(journeyService.getUser(),
                journeyService.getVehicle(),
                endStationId,
                journeyService.getEndPoint(),
                LocalDateTime.now(),
                this.journeyService.getAvgSpeed(),
                this.journeyService.getDistance(),
                this.journeyService.getDuration(),
                this.journeyService.getImporte());

        server.unPairRegisterService(journeyService);
        server.registerLocation(journeyService.getVehicle(), endStationId);
        pmVehicle.setAvailb();
        pmVehicle.setLocation(journeyService.getEndPoint());
        journeyService.setInProgress(false);
        arduino.undoBTconnection();

        // Mostrar confirmación
        System.out.println("Vehículo desemparejado correctamente");
        System.out.println("Importe total: " + journeyService.getImporte());
        System.out.println("Escoger método de pago");
    }

    private void calculateValues(GeographicPoint gP, LocalDateTime date) {
        if (pmVehicle == null) {
            throw new IllegalArgumentException("Vehicle no disponible");
        }
        float distance = calculateDistance(gP);
        int minutes = calculateDuration(date);
        calculateAvgSpeed(distance, minutes);
    }

    private float calculateDistance(GeographicPoint gP) {
        float distance = journeyService.getOriginPoint().calculateDistance(gP);
        journeyService.setDistance(distance);
        return distance;
    }

    private int calculateDuration(LocalDateTime date) {
        LocalTime initTime = journeyService.getInitHour();
        LocalTime endTime = date.toLocalTime();
        journeyService.setEndHour(endTime);
        return journeyService.setDuration(initTime, endTime);
    }

    private void calculateAvgSpeed(float distance, int minutes) {
        journeyService.setAvgSpeed(distance, minutes);
    }

    private void calculateImport(float distance, int duration, float averageSpeed, LocalDateTime date) {
        float importe = (distance * COST_PER_KM) + (duration * COST_PER_MINUTE);
        importe = Math.round(importe * 100) / 100f;
        BigDecimal amount = new BigDecimal(importe);
        journeyService.setImporte(amount);
    }


// Setter methods for injecting dependencies

    public void setStation(StationID station, boolean isOrigin) {
        if (isOrigin) {
            this.originStationId = station;
            journeyService.setOriginPoint(station.getLocation());
        } else {
            this.endStationId = station;
            journeyService.setEndPoint(station.getLocation());
        }
    }

}