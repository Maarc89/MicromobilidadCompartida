package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JourneyService {

    // ATRIBUTS a tenir en compte
    private UserAccount user;       // Usuario que realiza el viaje
    private VehicleID vehicle;      // Vehículo utilizado en el viaje
    private StationID startStation; // Estación de inicio
    private StationID endStation; // Estación de final

    // ATRIBUTS CLASSE del JPG
    private int duration;                 // Duración del viaje en minutos
    private float distance;               // Distancia recorrida
    private float avgSpeed;               // Velocidad promedio durante el viaje
    private GeographicPoint originPoint; // Ubicación de inicio
    private LocalDate initDate;      // Fecha de inicio del servicio
    private LocalTime initHour;      // Hora de inicio del servicio
    private GeographicPoint endPoint; // Ubicación de finalización
    private LocalDate endDate;        // Fecha de finalización del servicio
    private LocalTime endHour;        // Hora de finalización del servicio
    private BigDecimal importe;       // Import final
    private String serviceID;           // Identificador del viaje
    private boolean inProgress;


    public JourneyService(UserAccount user, VehicleID vehicle, LocalDate initDate, LocalTime initHour, GeographicPoint location, StationID startStationID){
        this.user = user;
        this.vehicle = vehicle;
        this.initDate = initDate;
        this.initHour = initHour;
        this.originPoint = location;
        this.startStation = startStationID;
    }

    public JourneyService(){}


    public void setStartStation(StationID startStation){
        this.startStation = startStation;
    }

    public int setDuration(LocalTime initHour, LocalTime endHour) {
        if (initHour != null && endHour != null) {
            Duration duration = Duration.between(initHour, endHour);
            this.duration = (int) duration.toMinutes();
        }
        return duration;
    }

    public void setDuration1(int duration){
        this.duration = duration;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setAvgSpeed(float distance,float duration) {
        this.avgSpeed = distance / duration*60;
    }

    public void setOriginPoint(GeographicPoint gp){
        this.originPoint = gp;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public void setInitHour(LocalTime initHour) {
        this.initHour = initHour;
    }

    public void setEndPoint(GeographicPoint gp){
        this.endPoint = gp;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public void setImporte(BigDecimal importe){
        this.importe = importe;
    }

    public void setServiceID(String id){
        this.serviceID = id;

    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
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


    // Getters

    public UserAccount getUser() {
        return user;
    }

    public VehicleID getVehicle() {
        return vehicle;
    }

    public StationID getStartStation() {
        return startStation;
    }

    public StationID getEndStation() {
        return endStation;
    }

    public int getDuration() {
        return duration;
    }

    public float getDistance() {
        return distance;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public GeographicPoint getOriginPoint() {
        return originPoint;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public LocalTime getInitHour() {
        return initHour;
    }

    public GeographicPoint getEndPoint() {
        return endPoint;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public BigDecimal getImporte(){
        return importe;
    }

    public String getServiceID() {
        return serviceID;
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
