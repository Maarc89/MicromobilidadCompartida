package data;

import java.util.Objects;

public final class VehicleID {

    // La id serà de longitud fija (7 caracteres), serà una combinación de letras y números
    // y solo podrà contener letras mayúsculas (2, en concreto seràn siempre VH)
    // y números (5 en concreto).
    private final String vehicleID;

    // Constructor
    public VehicleID(String vehicleID) {
        if (vehicleID == null) {
            throw new NullPointerException("VehicleID no puede ser nulo.");
        }
        if (!isValid(vehicleID)) {
            throw new IllegalArgumentException("VehicleID mal formado. Debe comenzar con 'VH' seguido de 5 dígitos.");
        }
        this.vehicleID = vehicleID;
    }

    // Validación del formato
    private boolean isValid(String vehicleID) {
        return vehicleID.matches("^VH\\d{5}$"); // "VH" seguido de 5 números
    }

    // Getter
    public String getId() {
        return vehicleID;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleID vehicleID = (VehicleID) o;
        return Objects.equals(vehicleID, vehicleID.vehicleID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleID);
    }

    // toString
    @Override
    public String toString() {
        return "VehicleID{" + "id='" + vehicleID + '\'' + '}';
    }
}
