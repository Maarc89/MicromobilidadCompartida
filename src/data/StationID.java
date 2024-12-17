package data;

import java.util.Objects;

public class StationID {

    // stationID serà de longitud fija (6 caracteres), serà una combinación de letras y números
    // y solo podrà contener letras mayúsculas (2 en concreto) y números (4 en concreto).

    private final String stationID;

    public StationID(String stationID) {
        if (stationID == null) {
            throw new NullPointerException("StationID no puede ser nulo.");
        }
        if (!isValid(stationID)) {
            throw new IllegalArgumentException("StationID mal formado. Debe ser alfanumérico y de longitud 6.");
        }
        this.stationID = stationID;
    }

    private boolean isValid(String stationID) {
        return stationID.matches("^[A-Z]{2}\\d{4}$"); // 2 letras mayúsculas + 4 números
    }

    // Getter
    public String getId() {
        return stationID;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationID stationID = (StationID) o;
        return Objects.equals(stationID, stationID.stationID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationID);
    }

    // toString
    @Override
    public String toString() {
        return "StationID{" +
                "id='" + stationID + '\'' + '}';
    }


}
