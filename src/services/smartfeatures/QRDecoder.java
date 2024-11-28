package services.smartfeatures;

import java.awt.image.BufferedImage;

/**
 * Interface for decoding QR codes from an image
 */
public interface QRDecoder {
    // Decodes QR codes from an image
    VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException;
}
