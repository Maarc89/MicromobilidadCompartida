package services.smartfeatures;

import data.VehicleID;
import exceptions.CorruptedImgException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class TestQRDecoder {

    private MockQRDecoder validMock;
    private MockQRDecoder invalidMock;
    private BufferedImage mockImage;

    @BeforeEach
    void setUp() {
        // Mock para un QR válido
        VehicleID validVehicleID = new VehicleID("VH12345");
        validMock = new MockQRDecoder(validVehicleID, true);

        // Mock para un QR inválido
        invalidMock = new MockQRDecoder(null, false);

        // Imagen simulada (puede ser null, ya que MockQRDecoder no la procesa realmente)
        mockImage = null;
    }

    @Test
    void testValidQRDecoding() throws CorruptedImgException {
        // Act
        VehicleID vehicleID = validMock.getVehicleID(mockImage);

        // Assert
        assertNotNull(vehicleID);
        assertEquals("VH12345", vehicleID.getId());
    }

    @Test
    void testInvalidQRDecodingThrowsException() {
        // Act & Assert
        CorruptedImgException exception = assertThrows(CorruptedImgException.class, () -> {
            invalidMock.getVehicleID(mockImage);
        });
        assertEquals("Imatge no acceptada", exception.getMessage());
    }
}

