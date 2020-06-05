package pt.ua.deti.shared.stubs;

import java.io.Closeable;

/**
 * General Repository of Information Interface
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public interface GRIInterface extends Closeable {

    /**
     * Write the initial header.
     */
    public void writeHeader();

    /**
     * Write one line.
     */
    public void writeLine();

    /**
     * Write the final report.
     */
    public void writeReport();

    /**
     * Update the number of passengers which have this airport as their final
     * destination.
     * 
     * @param inc the amount to increase
     */
    public void updateTRT(final int inc);

    /**
     * Update the number of passengers in transit.
     * 
     * @param inc the amount to increase
     */
    public void updateFDT(final int inc);

    /**
     * Update de number of bags that should have been transported in the the planes
     * hold.
     * 
     * @param inc the amount to increase
     */
    public void updateBags(final int inc);

    /**
     * Update de number of bags that were lost.
     * 
     * @param inc the amount to increase
     */
    public void updateMissing(final int inc);

    /**
     * Update the {@link pt.ua.deti.common.Plane} state.
     * 
     * @param fn flight number
     * @param bn number of pieces of luggage presently at the plane's hold
     */
    public void updatePlane(final int fn, final int bn);

    /**
     * Update the {@link PlaneHold} state.
     * 
     * @param bn number of pieces of luggage presently at the plane's hold
     */
    public void updatePlaneHold(final int bn);

    /**
     * Update the {@link pt.ua.deti.entities.Porter} state.
     * 
     * @param pstat the {@link pt.ua.deti.entities.Porter} state
     */
    public void updatePStat(final int pstat);

    /**
     * Update the number of pieces of luggage presently on the conveyor belt.
     * 
     * @param cb number of pieces of luggage presently on the conveyor belt
     */
    public void updateConveyorBelt(final int cb);

    /**
     * Update the number of pieces of luggage belonging to passengers in transit
     * presently stored at the storeroom.
     * 
     * @param sr number of pieces of luggage belonging to passengers in transit
     *           presently stored at the storeroom
     */
    public void updateStoreroom(final int sr);

    /**
     * Update the {@link pt.ua.deti.entities.Passenger} information
     * 
     * @param id        the identification of the
     *                  {@link pt.ua.deti.entities.Passenger}
     * @param state     the new state of the {@link pt.ua.deti.entities.Passenger}
     * @param situation the situation of the {@link pt.ua.deti.entities.Passenger}
     * @param bags      number of pieces of luggage the
     *                  {@link pt.ua.deti.entities.Passenger} carried at the start
     *                  of her journey
     * @param collected number of pieces of luggage the
     *                  {@link pt.ua.deti.entities.Passenger} she has presently
     *                  collected
     */
    public void updatePassenger(final int id, final int state, final int situation, final int bags,
            final int collected);
    
    /**
     * Update the queue of {@link pt.ua.deti.entities.Passenger} waiting for the
     * bus.
     * <p>
     * It adds the {@link pt.ua.deti.entities.Passenger} to the queue.
     * </p>
     * 
     * @param id the identification of the {@link pt.ua.deti.entities.Passenger}
     */
    public void updateQueueAdd(final int id);

    /**
     * Update the queue of {@link pt.ua.deti.entities.Passenger} waiting for the
     * bus.
     * <p>
     * It removes the {@link pt.ua.deti.entities.Passenger} from the queue. It has
     * to deslocate all the positions of the array.
     * </p>
     * 
     * @param id the identification of the {@link pt.ua.deti.entities.Passenger}
     */
    public void updateQueueRemove(final int id);

    /**
     * Update the queue of {@link pt.ua.deti.entities.Passenger} inside for the bus.
     * <p>
     * It adds the {@link pt.ua.deti.entities.Passenger} to the queue.
     * </p>
     * 
     * @param id the identification of the {@link pt.ua.deti.entities.Passenger}
     */
    public void updateSeatAdd(final int id);

    /**
     * Update the queue of {@link pt.ua.deti.entities.Passenger} inside for the bus.
     * <p>
     * It removes the {@link pt.ua.deti.entities.Passenger} from the queue.
     * </p>
     * 
     * @param id the identification of the {@link pt.ua.deti.entities.Passenger}
     */
    public void updateSeatRemove(final int id);

    /**
     * Update the {@link pt.ua.deti.entities.BusDriver} state.
     * 
     * @param bstat the {@link pt.ua.deti.entities.BusDriver} state
     */
    public void updateBusDriver(final int bstat);

    /**
     * Writes a debug message to the console of.
     * 
     * @param msg the message text ({@link String})
     */
    public void debug(final String msg);
}