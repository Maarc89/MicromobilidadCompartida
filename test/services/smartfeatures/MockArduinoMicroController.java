package services.smartfeatures;

import exceptions.ConnectException;
import exceptions.PMVPhysicalException;
import exceptions.ProceduralException;
import micromobility.JourneyRealizeHandler;
import services.smartfeatures.ArduinoMicroController;

public class MockArduinoMicroController implements ArduinoMicroController {

    private boolean isConected;
    private boolean phisicalError;
    private JourneyRealizeHandler jrh;

    public MockArduinoMicroController(Boolean isConected, Boolean phisicalError){
        this.isConected = isConected;
        this.phisicalError = phisicalError;
    }
    public MockArduinoMicroController(JourneyRealizeHandler jrh){
        this.jrh = jrh;
    }

    public void setFailureState(boolean failure) {
        this.isConected = !failure;
    }

    public void setBTconnection() throws ConnectException {
        if(isConected) throw new ConnectException("BT Alredy conected");
        isConected = true;
    }

    public void startDriving() throws PMVPhysicalException, ConnectException, ProceduralException {
        if(!isConected) throw new ConnectException("Must Be Connected First");
        if(!phisicalError) throw new PMVPhysicalException("Something Went Rong");
        jrh.startDriving();
    }

    public void stopDriving() throws PMVPhysicalException, ConnectException, ProceduralException {
        if(!isConected) throw new ConnectException("Must Be Connected First");
        if(!phisicalError) throw new PMVPhysicalException("Something Went Rong");
        jrh.stopDriving();
    }

    public void undoBTconnection() {
        isConected = false;
    }
}
