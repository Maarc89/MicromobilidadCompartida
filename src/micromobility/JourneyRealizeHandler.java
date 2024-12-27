package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.VehicleID;
import exceptions.*;
import services.Server;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.awt.image.BufferedImage;
import java.time.LocalTime;

public class JourneyRealizeHandler {


    private JourneyService journeyService;

    private GeographicPoint originalPoint;
    private QRDecoder qrdecoder;
    private UnbondedBTSignal bluetooth;
    private BufferedImage img;
    private Server server;
    private ArduinoMicroController arduino;

    private StationID orgStatId; //origin station id
    private PMVehicle pmVehicle; // primary mobility vehicle

    private BigDecimal importe;
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

        this.journeyService = s;
        System.out.println("Vehicle Pairing Completed");
        System.out.println("You can start driving");
    }


    public boolean isPMVehicleinZone() {

        //GeographicPoint stationLocation = orgStatId.g
        GeographicPoint vehicleLocation = pmVehicle.getLocation();


        return true;
    }

    public void unPairVehicle() throws ConnectException, InvalidPairingArgsException,
            PairingNotFoundException, ProceduralException {


        // Actualizar atributos en JourneyService
        this.journeyService.setEndPoint(pmVehicle.getLocation()); // Suponiendo que se obtiene la ubicación actual
        this.journeyService.setEndDate(LocalDate.now());
        this.journeyService.setEndHour(LocalTime.now());
        // calcular y modificar duración, distancia y velocidad promedio
        calculateValues(pmVehicle.getLocation(),LocalDateTime.now());
        // calcular y modificar el importe
        calculateImport(this.journeyService.getDistance(), this.journeyService.getDuration(), this.journeyService.getAvgSpeed(), LocalDateTime.now());
        //this.journeyService.setImporte(getImporte());
        // METODO PARA PAGO
        //serviceID
        int numeroAleatorio = (int) (Math.random() * 100) + 1;
        this.journeyService.setServiceID(numeroAleatorio);

        server.stopPairing(this.journeyService.getUser(),
                this.journeyService.getVehicle(),
                this.journeyService.getEndStation(),
                this.journeyService.get,
                LocalDateTime.now(),
                this.journeyService.getAvgSpeed(),
                this.journeyService.getDistance(),
                this.journeyService.getDuration(),
                this.journeyService.getImporte());

        pmVehicle.setAvailb();
        pmVehicle.setLocation(this.journeyService.getEndPoint());
        server.registerLocation(this.journeyService.getVehicle(), this.journeyService.getEndStation());
        server.unPairRegisterService(this.journeyService);
        this.journeyService.setInProgress(false);
        arduino.undoBTconnection();

        // Mostrar confirmación
        System.out.println("Vehículo desemparejado correctamente");
        System.out.println("Importe total: " + journeyService.getImporte());
        System.out.println("Escoger método de pago");
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
        this.journeyService.setDistance(distance);

        //duracio
        LocalTime initTime = this.journeyService.getInitHour();
        this.journeyService.setEndHour(date.toLocalTime());
        LocalTime endTime = date.toLocalTime();
        int minutes = this.journeyService.setDuration(initTime,endTime);

        //velocitat mitja
        this.journeyService.setAvgSpeed(distance, minutes);
    }


    private void calculateImport(float distance, int duration, float averageSpeed, LocalDateTime date) {
        float costoKm = 0.5f;
        float costoMinuto = 0.2f;

        //(distancia * costxKM) + (duracio * costXMinut)
        float importe = (distance * costoKm + duration * costoMinuto);
        // arrodonim a 2 decimals perls centims
        importe = Math.round(importe * 100) / 100f;
        BigDecimal amount = new BigDecimal(importe);
        journeyService.setImporte(amount);

    }



// Setter methods for injecting dependencies
}