package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class JourneyService {

    private final int serviceID;           // Identificador del viaje
    private final UserAccount user;       // Usuario que realiza el viaje
    private final VehicleID vehicle;      // Vehículo utilizado en el viaje
    private final StationID startStation; // Estación de inicio
    private final GeographicPoint startLocation; // Ubicación de inicio
    private LocalDateTime startTime;      // Hora de inicio del servicio
    private LocalDateTime endTime;        // Hora de finalización
    private float distance;               // Distancia recorrida
    private int duration;                 // Duración del viaje en minutos
    private float avgSpeed;               // Velocidad promedio durante el viaje
    private BigDecimal amount;
    private boolean inProgress;

    public JourneyService(int journeyID, UserAccount user, VehicleID vehicle, StationID startStation, GeographicPoint startLocation) {
        serviceID = journeyID;
        this.user = user;
        this.vehicle = vehicle;
        this.startStation = startStation;
        this.startLocation = startLocation;
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

    public GeographicPoint getStartLocation() {
        return startLocation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
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

    public boolean isInProgress() { return inProgress; }

    // The setter methods to be used
    public void setServiceInit(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setServiceFinish(LocalDateTime endTime, float distance, int duration, float avgSpeed, BigDecimal amount) {
        this.endTime = endTime;
        this.distance = distance;
        this.duration = duration;
        this.avgSpeed = avgSpeed;
        this.amount = amount;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void calculateDuration() {
        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
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
