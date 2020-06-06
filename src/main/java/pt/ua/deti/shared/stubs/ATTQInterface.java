package pt.ua.deti.shared.stubs;

import java.io.Closeable;

/**
 * Arrival Terminal Transfer Quay Interface.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public interface ATTQInterface extends Closeable {
    /**
     * Take a bus.
     * 
     * @param id the identification from the {@link pt.ua.deti.entities.Passenger}
     */
    public void takeABus(final int id);

    /**
     * Announcing Bus Boarding.
     * 
     * @return the number of passengers for this trip
     */
    public int announcingBusBoarding();

    /**
     * Enter the Bus.
     * 
     * @param id the identification from the {@link pt.ua.deti.entities.Passenger}
     */
    public void enterTheBus(final int id);

    /**
     * Go to Departure Terminal.
     * 
     * @param numberPassengers the current number of passenger for this trip
     */
    public void goToDepartureTerminal(final int numberPassengers);
}