package pt.ua.deti.shared.stubs;

import java.io.Closeable;
/**
 * Arrival Terminal Exit Interface.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public interface ATEInterface extends Closeable {
    /**
     * Reset the {@link DepartureTerminalEntrance} by setting the blocked to 0.
     * 
     * @param last flag used to identify if is the last plane
     */
    public void reset(final boolean last);

    /**
     * Go Home.
     * 
     * @param id the identification of the Passenger
     */
    public void goHome(final int id);

    /**
     * Returns the number of blocked {@link pt.ua.deti.entities.Passenger}.
     * 
     * @return the number of blocked {@link pt.ua.deti.entities.Passenger}
     */
    public int getBlocked();

    /**
     * Has days work ended.
     * 
     * @return true if the simulation is finished; otherwise false
     */
    public boolean hasDaysWorkEnded();
}