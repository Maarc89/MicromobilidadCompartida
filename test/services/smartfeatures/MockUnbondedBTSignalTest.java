package services.smartfeatures;

import exceptions.ConnectException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockUnbondedBTSignalTest {

    @Test
    void testBTbroadcastSuccess() {
        // Crear un mock que simule una transmisión exitosa
        UnbondedBTSignal mockSignal = new MockUnbondedBTSignal(false);

        assertDoesNotThrow(() -> {
            mockSignal.BTbroadcast(); // No debería lanzar ninguna excepción
        });

        System.out.println("Test de transmisión exitosa completado.");
    }

    @Test
    void testBTbroadcastFailure() {
        // Crear un mock que simule un fallo de conexión
        UnbondedBTSignal mockSignal = new MockUnbondedBTSignal(true);

        ConnectException exception = assertThrows(ConnectException.class, () -> {
            mockSignal.BTbroadcast(); // Debería lanzar una excepción
        });

        assertEquals("Simulated connection failure", exception.getMessage());
        System.out.println("Test de transmisión fallida completado.");
    }
}
