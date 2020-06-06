package pt.ua.deti.common;

import java.io.Serializable;

/**
 * This class represents a bag.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class Bag implements Serializable {
    /** serialization runtime associates with each serializable class a version number */
    private static final long serialVersionUID = 1L;
    /** {@link pt.ua.deti.common.Bag} id */
    private final int id;
    /** flags if a bag is in transit */
    private final boolean transit;

    /**
     * Create a {@link pt.ua.deti.common.Bag}.
     * 
     * @param id      id of the {@link pt.ua.deti.common.Bag}
     * @param transit flags if a bag is in transit
     */
    public Bag(final int id, final boolean transit) {
        this.id = id;
        this.transit = transit;
    }

    /**
     * Returns the id from the {@link pt.ua.deti.common.Bag}.
     * 
     * @return the id from the {@link pt.ua.deti.common.Bag}
     */
    public int id() {
        return id;
    }

    /**
     * Returns true if the bag bellows to a {@link pt.ua.deti.entities.Passenger} in transit; otherwise
     * false.
     * 
     * @return true if the bag bellows to a {@link pt.ua.deti.entities.Passenger} in transit; otherwise
     *         false
     */
    public boolean transit() {
        return transit;
    }
}
