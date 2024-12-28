package smartfeatures;

import data.VehicleID;
import exceptions.CorruptedImgException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.QRDecoder;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestDoblesQRDecoder {

    private QRDecoder qrDecoderMock;
    private BufferedImage validImage;
    private BufferedImage corruptedImage;
    private VehicleID expectedVehicleID;

    @BeforeEach
    void setUp() {
        // Crear el mock
        qrDecoderMock = mock(QRDecoder.class);

        // Crear objectes simulats
        validImage = mock(BufferedImage.class);
        corruptedImage = mock(BufferedImage.class);
        expectedVehicleID = new VehicleID("ABC123");

        try {
            // Configurar comportamiento del mock
            when(qrDecoderMock.getVehicleID(validImage)).thenReturn(expectedVehicleID);
            when(qrDecoderMock.getVehicleID(corruptedImage)).thenThrow(new CorruptedImgException("Imagen corrupta"));
        } catch (CorruptedImgException e) {
            fail("Configuración de prueba incorrecta");
        }
    }

    @Test
    void testGetVehicleIDWithValidImage() {
        try {
            VehicleID result = qrDecoderMock.getVehicleID(validImage);
            assertNotNull(result);
            assertEquals(expectedVehicleID, result);
        } catch (CorruptedImgException e) {
            fail("No debería lanzar excepción para imágenes válidas");
        }
    }

    @Test
    void testGetVehicleIDWithCorruptedImage() {
        assertThrows(CorruptedImgException.class, () -> {
            qrDecoderMock.getVehicleID(corruptedImage);
        });
    }
}
