package micromobility;

import data.GeographicPoint;

/**
 * Internal classes involved in the use of the service
 */
public class PMVehicle {

    // The class members
    private GeographicPoint location;
    private PMVState state;


    // Constructor
    public PMVehicle(PMVState state, GeographicPoint location) {
        this.state = state;
        this.location = location;
    }

    // Getter methods
    public PMVState getState() {
        return state;
    }

    public GeographicPoint getLocation() {
        return location;
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

