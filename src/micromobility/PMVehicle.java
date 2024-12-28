package micromobility;

import data.GeographicPoint;
import data.VehicleID;
import services.smartfeatures.QRDecoder;

import java.awt.image.BufferedImage;

/**
 * Internal classes involved in the use of the service
 */
public class PMVehicle {

    // The class members
    private GeographicPoint location;
    private BufferedImage qrImage;
    private VehicleID id;
    private PMVState state;

    public PMVehicle(VehicleID id, PMVState state, GeographicPoint location, BufferedImage qrImage) {
        this.state = state;
        this.location = location;
        this.qrImage = qrImage;
        this.id = id;
    }

    // Getter methods
    public PMVState getState() {
        return state;
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public BufferedImage getQr() {return qrImage;}

    public VehicleID getId() {
        return id;
    }

    // The setter methods to be used are only the following ones
    public void setNotAvailb() {
        this.state = PMVState.NotAvailable;
    }

    public void setUnderWay() {
        this.state = PMVState.UnderWay;
    }

    public void setAvailb() {
        this.state = PMVState.Available;
    }

    public void setLocation(GeographicPoint gP) {
        this.location = gP;
    }

    public enum PMVState { Available, NotAvailable, UnderWay, TemporaryParking;}
}

