package services;

import data.*;
import exceptions.ConnectException;
import exceptions.InvalidPairingArgsException;
import exceptions.PMVNotAvailException;
import exceptions.PairingNotFoundException;
import micromobility.JourneyRealizeHandler;
import micromobility.JourneyService;
import micromobility.PMVehicle;
import services.Server;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MockServer implements Server {
    private GeographicPoint loc;
    private BufferedImage qrImage;
    private PMVehicle.PMVState state;
    private boolean conectionError;


    public MockServer (GeographicPoint loc, PMVehicle.PMVState state, BufferedImage qrImage){
        this.loc = loc;
        this.state = state;
        this.qrImage = qrImage;
    }

    public  MockServer (boolean conectionError){
        this.conectionError = conectionError;
    }

    private JourneyRealizeHandler jrh;

    public MockServer (JourneyRealizeHandler jrh){
        this.jrh = jrh;
    }

    @Override
    public void checkPMVAvail(VehicleID veh) throws PMVNotAvailException, ConnectException {
        if(conectionError) throw new ConnectException("Conecction Error");
        PMVehicle vehicle = new PMVehicle(veh, state, loc, qrImage);
        if(vehicle.getState() == PMVehicle.PMVState.NotAvailable) throw new PMVNotAvailException("Vehicle not available, is connected to other user");
    }

    @Override
    public void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) throws InvalidPairingArgsException, ConnectException {
        if(conectionError) throw new ConnectException("Conecction Error");


    }

    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date, float avSp, float dist, int dur, BigDecimal imp) throws InvalidPairingArgsException, ConnectException {
        if(conectionError) throw new ConnectException("Conecction Error");
        if(user == null || veh == null || st == null || loc == null || date == null || avSp <= 0 || dist <= 0 || dur <= 0 || imp == null) {
            throw new InvalidPairingArgsException("Some arguments are not valid");
        }
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) {

    }

    @Override
    public void unPairRegisterService(JourneyService s) throws PairingNotFoundException {

    }

    @Override
    public void registerLocation(VehicleID veh, StationID st) {

    }

    @Override
    public void registerPayment(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) throws ConnectException {
        if(conectionError) throw new ConnectException("Conecction Error");

    }
}
