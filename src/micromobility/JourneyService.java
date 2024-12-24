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
    private final UserAccount user;       // Usuario que realiza el viaje
    private final VehicleID vehicle;      // Vehículo utilizado en el viaje
    private final StationID startStation; // Estación de inicio

    // ATRIBUTS CLASSE del JPG
    private final int serviceID;           // Identificador del viaje
    private GeographicPoint originPoint; // Ubicación de inicio
    private GeographicPoint endPoint; // Ubicación de finalización
    private LocalTime initHour;      // Hora de inicio del servicio
    private LocalTime endHour;        // Hora de finalización del servicio
    private LocalDate initDate;      // Fecha de inicio del servicio
    private LocalDate endDate;        // Fecha de finalización del servicio
    private float distance;               // Distancia recorrida
    private int duration;                 // Duración del viaje en minutos
    private float avgSpeed;               // Velocidad promedio durante el viaje
    private BigDecimal amount;
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

    public void setEndPoint(){
        this.endPoint = endPoint;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }



    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public JourneyService(int journeyID, UserAccount user, VehicleID vehicle, StationID startStation, GeographicPoint originPoint, GeographicPoint endPoint) {
        serviceID = journeyID;
        this.user = user;
        this.vehicle = vehicle;
        this.startStation = startStation;
        this.originPoint = originPoint;
        this.endPoint = endPoint;
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
        this.amount = amount;
    }

    public GeographicPoint getEndPoint() {
        return endPoint;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void calculateDuration() {
        if (initHour != null && endHour != null) {
            Duration duration = Duration.between(initHour, endHour);
            this.duration = (int) duration.toMinutes();
        }
    }

    public void calculateAvgSpeed() {
        if (duration > 0) {
            this.avgSpeed = distance / duration;
        }
    }

    public void calculateAmount(float rate) {
        this.amount = BigDecimal.valueOf(distance * rate);
    }

}
