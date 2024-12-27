package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class JourneyService {

    // ATRIBUTS a tenir en compte
    private UserAccount user;       // Usuario que realiza el viaje
    private VehicleID vehicle;      // Vehículo utilizado en el viaje
    private StationID startStation; // Estación de inicio

    public StationID getEndStation() {
        return endStation;
    }

    public void setEndStation(StationID endStation) {
        this.endStation = endStation;
    }

    private StationID endStation; // Estación de final

    // ATRIBUTS CLASSE del JPG
    private int serviceID;           // Identificador del viaje
    private GeographicPoint originPoint; // Ubicación de inicio
    private GeographicPoint endPoint; // Ubicación de finalización
    private LocalTime initHour;      // Hora de inicio del servicio
    private LocalTime endHour;        // Hora de finalización del servicio
    private LocalDate initDate;      // Fecha de inicio del servicio
    private LocalDate endDate;        // Fecha de finalización del servicio
    private float distance;               // Distancia recorrida
    private int duration;                 // Duración del viaje en minutos
    private float avgSpeed;               // Velocidad promedio durante el viaje
    private BigDecimal importe;
    private boolean inProgress;

    public void setInitHour(LocalTime initHour) {
        this.initHour = initHour;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public void setOriginPoint(float lat, float lon){
        this.originPoint = originPoint;
    }

    public void setEndPoint(GeographicPoint gp){
        this.endPoint = gp;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setServiceID(int id){
        this.serviceID = id;
    }

    public void setImporte(BigDecimal importe){
        this.importe = importe;
    }

    public JourneyService(int serviceID, UserAccount user, VehicleID vehicle, StationID startStation, GeographicPoint originPoint, GeographicPoint endPoint) {
        this.serviceID = serviceID;
        this.user = user;
        this.vehicle = vehicle;
        this.startStation = startStation;
        this.originPoint = originPoint;
        this.endPoint = endPoint;
    }

    public JourneyService(){

    }

    // The setter methods to be used
    public void setServiceInit() {
        this.initHour = LocalTime.now();
        this.initDate = LocalDate.now();
    }

    public void setServiceFinish(LocalTime endTime, float distance, int duration, float avgSpeed, BigDecimal amount) {
        this.endHour = endTime;
        this.distance = distance;
        this.duration = duration;
        this.avgSpeed = avgSpeed;
        this.importe = amount;
    }

    public GeographicPoint getEndPoint() {
        return endPoint;
    }

    public BigDecimal getImporte() {
        return importe;
    }



    // Getters
    public int getServiceID() {
        return serviceID;
    }

    public UserAccount getUser() {
        return user;
    }

    public VehicleID getVehicle() {
        return vehicle;
    }

    public StationID getStartStation() {
        return startStation;
    }

    public GeographicPoint getoriginPoint() {
        return originPoint;
    }

    public LocalTime getInitHour() {
        return initHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public float getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public int setDuration(LocalTime initHour, LocalTime endHour) {
        if (initHour != null && endHour != null) {
            Duration duration = Duration.between(initHour, endHour);
            this.duration = (int) duration.toMinutes();
        }
        return duration;
    }

    public void setAvgSpeed(float distance,float duration) {
            this.avgSpeed = distance / duration*60;
    }



}
