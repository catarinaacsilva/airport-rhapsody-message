package pt.ua.deti.common;

import java.util.List;

import pt.ua.deti.entities.Passenger;

/**
 * Class that represents a plane.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class Plane {
    /** {@link List} to the passengers of the plane */
    private final List<Passenger> passengers;
    /** {@link List} of the bags of the passengers */
    private final List<Bag> bags;
    /** Represents the flight number */
    private final int fn;

    /**
     * Creates a plane object
     * 
     * @param fn         flight number
     * @param passengers {@link List} of passengers
     * @param bags       {@link List} of bags
     */
    public Plane(final int fn, final List<Passenger> passengers, final List<Bag> bags) {
        this.fn = fn;
        this.passengers = passengers;
        this.bags = bags;
    }

    /**
     * Returns the flight number
     * 
     * @return the flight number
     */
    public int fn() {
        return fn;
    }

    /**
     * Returns the number of bags.
     * 
     * @return the number of bags
     */
    public int bn() {
        return bags.size();
    }

    /**
     * Returns the {@link List} of {@link pt.ua.deti.entities.Passenger}.
     * 
     * @return the {@link List} of {@link pt.ua.deti.entities.Passenger}
     */
    public List<Passenger> getPassengers() {
        return passengers;
    }

    /**
     * Returns the {@link List} of {@link pt.ua.deti.common.Bag}.
     * 
     * @return the {@link List} of {@link pt.ua.deti.common.Bag}
     */
    public List<Bag> getBags() {
        return bags;
    }
}
