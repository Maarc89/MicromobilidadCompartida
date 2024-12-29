package services.smartfeatures;

import data.VehicleID;
import exceptions.CorruptedImgException;

import java.awt.image.BufferedImage;

public class MockQRDecoder implements QRDecoder {

    private final VehicleID vehicleID;
    private final boolean isValid;

    public MockQRDecoder(VehicleID vehicleID, boolean isValid) {
        this.vehicleID = vehicleID;
        this.isValid = isValid;
    }

    @Override
    public VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException {
        if(!isValid) throw new CorruptedImgException("Imatge no acceptada");
        return vehicleID;
    }
}

