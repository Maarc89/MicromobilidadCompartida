package dobles;

import exceptions.ConnectException;
import services.smartfeatures.UnbondedBTSignal;

/**
 * Mock implementation of UnbondedBTSignal for testing purposes.
 */
public class MockUnbondedBTSignal implements UnbondedBTSignal {

    private final boolean shouldThrowException;

    /**
     * Constructor for MockUnbondedBTSignal.
     *
     * @param shouldThrowException If true, the mock will throw a ConnectException when BTbroadcast() is called.
     */
    public MockUnbondedBTSignal(boolean shouldThrowException) {
        this.shouldThrowException = shouldThrowException;
    }

    @Override
    public void BTbroadcast() throws ConnectException {
        if (shouldThrowException) {
            throw new ConnectException("Simulated connection failure");
        }
        // Simulate successful broadcasting
        System.out.println("Mock BT broadcast successful.");
    }
}
