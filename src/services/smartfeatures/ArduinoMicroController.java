package services.smartfeatures;

import exceptions.ConnectException;
import exceptions.PMVPhysicalException;
import exceptions.ProceduralException;

/**
 * Software for microcontrollers
 */
public interface ArduinoMicroController {

    // Sets up the Bluetooth connection
    public void setBTconnection() throws ConnectException;

    // Starts driving the vehicle
    public void startDriving() throws PMVPhysicalException, ConnectException, ProceduralException;

    // Stops driving the vehicle
    public void stopDriving() throws PMVPhysicalException, ConnectException, ProceduralException;

    // Undoes the Bluetooth connection
    public void undoBTconnection();
}
