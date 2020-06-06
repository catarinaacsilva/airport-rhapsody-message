package pt.ua.deti.shared.stubs;

import java.io.Closeable;

/**
 * Departure Terminal Entrance Interface.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public interface DTEInterface extends Closeable {
    /**
     * Reset the {@link DTEInterface} by setting the blocked to 0.
     */
    public void reset();

    /**
     * Prepare next leg.
     * 
     * @param id the identification of the Passenger
     */
    public void prepareNextLeg(final int id);

    /**
     * Returns the number of blocked {@link pt.ua.deti.entities.Passenger}.
     * 
     * @return the number of blocked {@link pt.ua.deti.entities.Passenger}
     */
    public int getBlocked();
}