package services;

import data.VehicleID;
import exceptions.ConnectException;
import exceptions.PMVNotAvailException;

public interface PasarelaPago {
    void enterCredentials(String credentials)
            throws ConnectException;

    void verifyCredentials(String credentials) throws ConnectException;
}
