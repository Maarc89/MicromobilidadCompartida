package micromobility;

import data.GeographicPoint;
import data.VehicleID;
import services.smartfeatures.QRDecoder;

/**
 * Internal classes involved in the use of the service
 */
public class PMVehicle {

    // The class members
    private PMVState state;
    private GeographicPoint location;
    private QRDecoder qr;
    private VehicleID id;

    public PMVehicle(PMVState state, GeographicPoint location, QRDecoder qr, VehicleID id) {
        this.state = state;
        this.location = location;
        this.qr = qr;
        this.id = id;
    }

    // Getter methods
    public PMVState getState() {
        return state;
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public QRDecoder getQr() {
        return qr;
    }

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

