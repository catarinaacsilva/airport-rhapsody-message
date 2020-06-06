package pt.ua.deti.entities;

import pt.ua.deti.shared.stubs.ATEInterface;
import pt.ua.deti.shared.stubs.ATTQInterface;
import pt.ua.deti.shared.stubs.DTEInterface;
import pt.ua.deti.shared.stubs.DTTQInterface;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * Bus Driver entity.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class BusDriver implements Runnable {
    /**
     * States that describe the life cycle of a
     * {@link pt.ua.deti.entities.BusDriver}
     */
    protected static enum State {
        PARKING_AT_THE_ARRIVAL_TERMINAL, DRIVING_FORWARD, PARKING_AT_THE_DEPARTURE_TERMINAL, DRIVING_BACKWARD
    }

    /** {@link State} the state of the {@link pt.ua.deti.entities.Porter} */
    private State state;
    /** {@link ATTQInterface} */
    private final ATTQInterface attq;
    /** {@link DTTQInterface} */
    private final DTTQInterface dttq;
    /** {@link ATEInterface} */
    private final ATEInterface ate;
    /** {@link DTEInterface} */
    private final DTEInterface dte;
    /** {@link GRIInterface} serves as log */
    private final GRIInterface gri;
    /** Flag used to indicate if the life cycle is done */
    private boolean done = false;
    /** The number of passengers for each trip */
    private int numberPassengers = 0;

    /**
     * Create a new {@link pt.ua.deti.entities.BusDriver}.
     * 
     * @param attq {@link ATTQInterface}
     * @param dttq {@link DTTQInterface}
     * @param ate  {@link ATEInterface}
     * @param dte  {@link DTEInterface}
     * @param gri  {@link GRIInterface} serves as log
     */
    public BusDriver(final ATTQInterface attq, final DTTQInterface dttq, final ATEInterface ate, final DTEInterface dte,
            final GRIInterface gri) {
        this.attq = attq;
        this.dttq = dttq;
        this.ate = ate;
        this.dte = dte;
        this.gri = gri;
        state = State.PARKING_AT_THE_ARRIVAL_TERMINAL;
    }

    @Override
    public void run() {
        while (!done) {
            switch (state) {
                case PARKING_AT_THE_ARRIVAL_TERMINAL:
                    // check if the day has finished
                    done = hasDaysWorkEnded();
                    if (!done) {
                        // announce the passengers that bus can go
                        numberPassengers = announcingBusBoarding();
                        if (numberPassengers > 0) {
                            goToDepartureTerminal(numberPassengers);
                        }
                    }
                    break;
                case DRIVING_FORWARD:
                    parkTheBusAndLetPassOff(numberPassengers);
                    break;
                case PARKING_AT_THE_DEPARTURE_TERMINAL:
                    goToArrivalTerminal();
                    break;
                case DRIVING_BACKWARD:
                    parkTheBus();
                    break;
                default:
                    break;
            }
        }

        try {
            ate.close();
            dte.close();
            dttq.close();
            attq.close();
            gri.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Park the bus.
     */
    private void parkTheBus() {
        state = State.PARKING_AT_THE_ARRIVAL_TERMINAL;
        gri.updateBusDriver(state.ordinal());
    }

    /**
     * Go To Arrival Terminal.
     */
    private void goToArrivalTerminal() {
        state = State.DRIVING_BACKWARD;
        gri.updateBusDriver(state.ordinal());
    }

    /**
     * Park The Bus and Let {@link pt.ua.deti.entities.Passenger} off.
     * 
     * @param numberPassengers the current number of passenger for this trip
     */
    private void parkTheBusAndLetPassOff(final int numberPassengers) {
        state = State.PARKING_AT_THE_DEPARTURE_TERMINAL;
        gri.updateBusDriver(state.ordinal());
        dttq.parkTheBusAndLetPassOff(numberPassengers);
    }

    /**
     * Announcing Bus Boarding.
     * 
     * @return the number of passengers for this trip
     */
    private int announcingBusBoarding() {
        // announce the passengers that bus can go
        state = State.PARKING_AT_THE_ARRIVAL_TERMINAL;
        gri.updateBusDriver(state.ordinal());
        return attq.announcingBusBoarding();
    }

    /**
     * Go to Departure Terminal.
     * 
     * @param numberPassengers the current number of passenger for this trip.
     */
    private void goToDepartureTerminal(final int numberPassengers) {
        // await for the passengers to enter the bus
        state = State.DRIVING_FORWARD;
        gri.updateBusDriver(state.ordinal());
        attq.goToDepartureTerminal(numberPassengers);
    }

    /**
     * Has days work ended.
     * 
     * @return true if the simulation is finished; otherwise false
     */
    private boolean hasDaysWorkEnded() {
        boolean rv = ate.hasDaysWorkEnded();
        return rv;
    }
}
