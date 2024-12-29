package micromobility;

import data.*;
import exceptions.*;
import micromobility.payment.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.MockServer;
import services.smartfeatures.MockQRDecoder;
import services.smartfeatures.MockUnbondedBTSignal;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JourneyRealizeHandlerTest {

    private JourneyRealizeHandler handler;
    private MockServer mockServer;
    private MockQRDecoder mockQRDecoder;
    private MockUnbondedBTSignal mockBluetooth;
    private PMVehicle mockVehicle;
    private StationID originStation;
    private BufferedImage mockImage;

    @BeforeEach
    void setUp() {
        // Inicializamos las clases mock
        mockServer = new MockServer(true);
        mockQRDecoder = new MockQRDecoder();
        mockBluetooth = new MockUnbondedBTSignal();
        mockVehicle = new PMVehicle(new VehicleID("VH12345"), PMVehicle.PMVState.Available, new GeographicPoint(0, 0), mockImage);
        originStation = new StationID("ST123");
        mockImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // Configuramos el vehículo como disponible
        mockServer.setVehicleAvailability(mockVehicle.getId(), true);

        // Creamos el handler con las dependencias mock
        handler = new JourneyRealizeHandler(mockQRDecoder, originStation, mockVehicle, null, mockServer, mockImage, mockBluetooth);
    }

    @Test
    void testJourneyFlow() throws Exception {
        // Paso 1: Broadcast station ID
        handler.broadcastStationID("ST123");
        assertNotNull(handler.CurrentStationID, "La estación actual no debería ser nula después de broadcast.");

        // Paso 2: Escaneo del QR y pairing
        handler.scanQR();
        assertFalse(mockVehicle.isAvailable(), "El vehículo debería estar marcado como no disponible después del pairing.");

        // Paso 3: Iniciar el trayecto
        handler.startDriving();
        assertTrue(mockVehicle.isUnderWay(), "El vehículo debería estar en movimiento después de iniciar el trayecto.");

        // Paso 4: Finalizar el trayecto
        handler.unPairVehicle();
        assertFalse(mockVehicle.isUnderWay(), "El vehículo no debería estar en movimiento después de desemparejar.");
        assertTrue(mockVehicle.isAvailable(), "El vehículo debería estar disponible después del desemparejamiento.");

        // Verificamos que el importe fue calculado
        assertNotNull(handler.journeyService.getImporte(), "El importe del trayecto debería haberse calculado.");
    }

    @Test
    void testProceduralExceptionOnOutOfOrder() {
        // Intentar iniciar el trayecto antes de escanear el QR debería lanzar ProceduralException
        ProceduralException exception = assertThrows(ProceduralException.class, handler::startDriving);
        assertEquals("Precondition violated for starting the journey.", exception.getMessage());
    }

    @Test
    void testVehicleNotAvailable() {
        // Marcamos el vehículo como no disponible en el mock server
        mockServer.setVehicleAvailability(mockVehicle.getVehicleID(), false);

        PMVNotAvailException exception = assertThrows(PMVNotAvailException.class, handler::scanQR);
        assertEquals("PMV not available: V123", exception.getMessage());
    }

    @Test
    void testServerUnavailable() {
        // Configuramos el servidor como no disponible
        mockServer.setAvailable(false);

        ConnectException exception = assertThrows(ConnectException.class, handler::scanQR);
        assertEquals("Server is unavailable.", exception.getMessage());
    }

    @Test
    void testCalculateImport() throws Exception {
        // Realizamos el flujo completo hasta el cálculo del importe
        handler.broadcastStationID("ST123");
        handler.scanQR();
        handler.startDriving();
        handler.unPairVehicle();

        BigDecimal importe = handler.journeyService.getImporte();
        assertNotNull(importe, "El importe debería haberse calculado después de desemparejar.");
        assertTrue(importe.compareTo(BigDecimal.ZERO) > 0, "El importe debería ser mayor que 0.");
    }
}
