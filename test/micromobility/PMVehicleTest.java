package micromobility;

import data.GeographicPoint;
import data.VehicleID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PMVehicleTest {

    private PMVehicle pmVehicle;
    private GeographicPoint initialLocation;

    @BeforeEach
    void setUp() {
        try {
            BufferedImage qrImage = ImageIO.read(new File("src/micromobility/assets/qrcode-generado.png"));
            initialLocation = new GeographicPoint(10.0F, 20.0F);
            VehicleID id = new VehicleID("VH12345");
            pmVehicle = new PMVehicle(id, PMVehicle.PMVState.Available, initialLocation, qrImage);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al leer la imagen: " + e.getMessage());
        }
    }

    @Test
    void testInitialStateAndLocation() {
        assertEquals(PMVehicle.PMVState.Available, pmVehicle.getState());
        assertEquals(initialLocation, pmVehicle.getLocation());
    }

    @Test
    void testSetNotAvailable() {
        pmVehicle.setNotAvailb();
        assertEquals(PMVehicle.PMVState.NotAvailable, pmVehicle.getState());
    }

    @Test
    void testSetUnderWay() {
        pmVehicle.setUnderWay();
        assertEquals(PMVehicle.PMVState.UnderWay, pmVehicle.getState());
    }

    @Test
    void testSetAvailable() {
        pmVehicle.setAvailb();
        assertEquals(PMVehicle.PMVState.Available, pmVehicle.getState());
    }

    @Test
    void testSetLocation() {
        GeographicPoint newLocation = new GeographicPoint(30.0F, 40.0F);
        pmVehicle.setLocation(newLocation);
        assertEquals(newLocation, pmVehicle.getLocation());
    }

    @Test
    void testTemporaryParkingState() {
        pmVehicle.setUnderWay(); // change to a state before testing state consistency
        assertNotEquals(PMVehicle.PMVState.TemporaryParking, pmVehicle.getState());
    }
}
