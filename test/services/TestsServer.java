package services;

import data.*;
import exceptions.*;
import micromobility.JourneyRealizeHandler;
import micromobility.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.MockQRDecoder;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsServer {
    private MockServer validMock;
    private MockServer invalidMock;

    private JourneyService journeyService;

    private VehicleID vehicle;
    private StationID station;
    private UserAccount user;
    private GeographicPoint location;
    private int duration;
    private float distance;
    private float avrSpeed;
    private BigDecimal importr;
    private ServiceID service;
    private char paymethod;



    @BeforeEach
    void setUp() {
        VehicleID validVehicleID = new VehicleID("VH12345");
        validMock = new MockServer(validVehicleID, true);

        // Mock para un QR inválido
        invalidMock = new MockQRDecoder(null, false);

        // Imagen simulada (puede ser null, ya que MockQRDecoder no la procesa realmente)
        mockImage = null;
    }

}

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


