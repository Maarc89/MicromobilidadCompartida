package smartfeatures;

import exceptions.ConnectException;
import exceptions.PMVPhysicalException;
import exceptions.ProceduralException;
import micromobility.JourneyRealizeHandler;
import services.smartfeatures.ArduinoMicroController;

public class MockArduinoMicroController implements ArduinoMicroController {

    private boolean isConected;

    public MockArduinoMicroController(Boolean isConected){
        this.isConected = isConected;
    }

    @Override
    public void setBTconnection() throws ConnectException {
        if(isConected) throw new ConnectException("Alredy conected");
        isConected = true;
    }

    @Override
    public void startDriving() throws PMVPhysicalException, ConnectException, ProceduralException {
        if(!isConected) throw new ConnectException("Must Be Connected First");
        try{
            //JourneyRealizeHandler.startDriving();
        }catch (Exception e) {
            throw new ProceduralException("unaveilable to startDriving");
        }
    }

    @Override
    public void stopDriving() throws PMVPhysicalException, ConnectException, ProceduralException {
        if(!isConected) throw new ConnectException("Must Be Connected First");
        try{
            //JourneyRealizeHandler.stopDriving();
        }catch (Exception e) {
            throw new ProceduralException("unaveilable to stopDriving");
        }
    }

    @Override
    public void undoBTconnection() {
        isConected = false;
    }
}
