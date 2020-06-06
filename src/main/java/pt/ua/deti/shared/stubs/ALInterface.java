package pt.ua.deti.shared.stubs;

import java.io.Closeable;

/**
 * Arrival Lounge Interface.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public interface ALInterface extends Closeable {
    /**
     * Reset the {@link ALInterface} by setting the disembark at zero.
     */
    public void reset();

    /**
     * The {@link pt.ua.deti.entities.Porter} takes a rest and waits for all the
     * {@link pt.ua.deti.entities.Passenger} to leave the
     * {@link pt.ua.deti.common.Plane}.
     */
    public void takeARest();

    /**
     * The {@link pt.ua.deti.entities.Passenger} indicates that has left the
     * {@link pt.ua.deti.common.Plane}.
     */
    public void whatShouldIDo();
}