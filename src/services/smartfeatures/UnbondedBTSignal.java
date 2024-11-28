package services.smartfeatures;

/**
 * External services involved in the functioning of some features
 */
public interface UnbondedBTSignal {
    // Broadcasts the station ID by BT
    void BTbroadcast() throws ConnectException;
}
