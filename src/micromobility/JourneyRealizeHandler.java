package micromobility;

import data.*;
import exceptions.*;
import micromobility.payment.Payment;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import services.Server;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

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

    private BigDecimal importe;

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

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = new Random().ints(5, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .reduce("", String::concat);
        journeyService.setServiceID(randomString);



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

    public void selectPaymentMethod (char option) throws ProceduralException, NotEnoughWalletException, ConnectException {
        realizePayment(importe);
        List<Object> credentials = List.of(user, user.getPassword(), user.getName());
        enterCredentials(credentials);
    }

    private void realizePayment (BigDecimal imp) throws NotEnoughWalletException, ConnectException {
        Payment p = new Payment(user, journeyService, imp);
        ServiceID s = new ServiceID(journeyService.getServiceID());
        server.registerPayment(s, user, importe, 'W');
        s.setImporte(imp);
        WalletPayment w = new WalletPayment(p);
        Wallet wallet = new Wallet(w);
        wallet.deduct(imp);
        List<Object> billData = List.of(s,wallet.getBalance());
        sendBillEmail(user.getEmail(), billData);
        System.out.println("Processing Payment");
    }

    private void enterCredentials(List<Object> credentials){
        UserAccount userAcc = (UserAccount) credentials.get(0);
        System.out.println("Validating credentials for: " + userAcc.getName());

        boolean isValid = verifyCredentials(credentials);
        if (!isValid) {
            throw new RuntimeException("Invalid credentials provided.");
        }
        System.out.println("Credentials validated.");
    }

    private boolean verifyCredentials(List<Object> credentials){
        UserAccount userAcc = (UserAccount) credentials.get(0);
        String password = (String) credentials.get(1);
        String name = (String) credentials.get(2);

        return userAcc.getPassword().equals(password) && userAcc.getName().equals(name);
    }

    public void confirmPayment(){
        Random random = new Random();
        int nTrans = random.nextInt(1000) + 1;
        askForAproval(nTrans, importe, LocalDateTime.now());
    }

    private void askForAproval(int nTrans, BigDecimal importe, LocalDateTime date){
        System.out.println("Requesting approval for transaction ID: " + nTrans);
        System.out.println("Amount: " + importe + ", Date: " + date);
        System.out.println("Approval granted.");
    }

    private void sendBillEmail(String email, List<Object> billdata){

        ServiceID trasanctionID = (ServiceID) billdata.get(0);
        BigDecimal balance = (BigDecimal) billdata.get(1);

        System.out.println("Payment succesfully DONE...");
        System.out.println("User: " + user.getUserAccount());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Amount: " + importe);
        System.out.println("Saldo restante: " + balance);
        System.out.println("Hour: " + LocalDateTime.now());
        System.out.println("TransactionID: " + trasanctionID);
        System.out.println("Payment Method: Wallet Payment");
        sendEmail(email, billdata);
    }

    private void sendEmail(String email, List<Object> billdata){
        System.out.println("Email Sent");
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
        // PER A LO UNIC QUE FAIG EL SETTER ES PEQUE NO SE COM INICIALITZAR
        // EL IMPORT dins de selectPAYMENT
        setImportxPayment(amount);
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

    public void setImportxPayment (BigDecimal importe){
        this.importe = importe;
    }

}