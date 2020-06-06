package pt.ua.deti.shared.stubs;

import java.io.Closeable;

/**
 * Departure Terminal Transfer Quay Interface.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public interface DTTQInterface extends Closeable {
    /**
     * Leave the bus.
     * 
     * @param id the identification from the {@link pt.ua.deti.entities.Passenger}
     */
    public void leaveTheBus(final int id);

    /**
     * Park The Bus and Let {@link pt.ua.deti.entities.Passenger} off.
     * 
     * @param numberPassengers the current number of passenger for this trip
     */
    public void parkTheBusAndLetPassOff(final int numberPassengers);
}