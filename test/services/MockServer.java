package services;

import data.*;
import exceptions.ConnectException;
import exceptions.InvalidPairingArgsException;
import exceptions.PMVNotAvailException;
import exceptions.PairingNotFoundException;
import micromobility.JourneyService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MockServer implements Server {

    private JourneyService journeyService;
    private boolean isAvailable;
    private final Map<VehicleID, Boolean> vehicleAvailability;
    private final Map<UserAccount, JourneyService> activePairings;

    public MockServer(boolean isAvailable) {
        this.isAvailable = isAvailable;
        this.vehicleAvailability = new HashMap<>();
        this.activePairings = new HashMap<>();
    }

    public void setServerConnected(boolean connected) {
        this.isAvailable = connected;
    }
    public void setJourneyService(JourneyService journeyService) {
        this.journeyService = journeyService;
    }
    public void setVehicleAvailability(VehicleID vhID, boolean available) {
        vehicleAvailability.put(vhID, available);
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @Override
    public void checkPMVAvail(VehicleID vhID) throws PMVNotAvailException, ConnectException {
        if (!isAvailable) {
            throw new ConnectException("Server is unavailable.");
        }
        Boolean available = vehicleAvailability.get(vhID);
        if (available != null && !available) {
            throw new PMVNotAvailException("PMV not available: " + vhID.getId());
        }
    }

    @Override
    public void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date)
            throws InvalidPairingArgsException, ConnectException {
        if (!isAvailable) {
            throw new ConnectException("Server is unavailable.");
        }
        if (activePairings.containsKey(user)) {
            throw new InvalidPairingArgsException("User already has an active pairing.");
        }

        JourneyService service = new JourneyService(user, veh, date.toLocalDate(), date.toLocalTime(), loc, st);
        activePairings.put(user, service);
    }

    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date, float avSp, float dist, int dur, BigDecimal imp)
            throws InvalidPairingArgsException, ConnectException {
        if (!isAvailable) {
            throw new ConnectException("Server is unavailable.");
        }
        JourneyService service = activePairings.remove(user);
        if (service == null) {
            throw new InvalidPairingArgsException("No active pairing found for user: " + user.getUserAccount());
        }
        service.setEndDate(date.toLocalDate());
        service.setAvgSpeed(dist, dur);
        service.setDistance(dist);
        service.setDuration1(dur);
        service.setImporte(imp);
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) {
        JourneyService service = new JourneyService(user, veh, date.toLocalDate(), date.toLocalTime(), loc, st);
        activePairings.put(user, service);
    }

    @Override
    public void unPairRegisterService(JourneyService s) throws PairingNotFoundException {
        if (!activePairings.containsKey(s.getUser())) {
            throw new PairingNotFoundException("No pairing found for user: " + s.getUser().getUserAccount());
        }
        activePairings.remove(s.getUser());
    }

    @Override
    public void registerLocation(VehicleID veh, StationID st) {

        System.out.println("Registered location: Vehicle " + veh.getId() + " at Station " + st.getId());
    }

    @Override
    public void registerPayment(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) throws ConnectException {
        if (!isAvailable) {
            throw new ConnectException("Server is unavailable.");
        }

        System.out.println("Payment registered: ServiceID " + servID.getServiceID() + ", User " + user.getUserAccount() + ", Amount " + imp);
    }
}
