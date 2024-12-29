package services.smartfeatures;

import data.StationID;
import exceptions.ConnectException;
import micromobility.JourneyRealizeHandler;

/**
 * Mock implementation of UnbondedBTSignal for testing purposes.
 */
public class MockUnbondedBTSignal implements UnbondedBTSignal {
    private JourneyRealizeHandler jrh;
    private boolean conectionError;
    private StationID stID;
    public MockUnbondedBTSignal(JourneyRealizeHandler jrh){
        this.jrh = jrh;
    }


    @Override
    public void BTbroadcast() throws ConnectException {
        if(!conectionError) throw new ConnectException("Conection Error");
        jrh.broadcastStationID(stID);
    }
}
