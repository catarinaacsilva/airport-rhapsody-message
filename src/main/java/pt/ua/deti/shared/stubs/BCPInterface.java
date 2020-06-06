package pt.ua.deti.shared.stubs;

import java.io.Closeable;

import pt.ua.deti.common.Bag;

/**
 * Baggage Collection Point Interface.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public interface BCPInterface extends Closeable {
    /**
     * Method used by the porter to store bags in the conveyor belt.
     * 
     * @param b {@link pt.ua.deti.common.Bag} carry be the
     *          {@link pt.ua.deti.entities.Porter}
     */
    public void storeBag(final Bag b);
    
    /**
     * Method used by the {@link pt.ua.deti.entities.Passenger} to collect the bags
     * 
     * @param bagId the id of the {@link pt.ua.deti.common.Bag}
     * @return True if the bag was collected, otherwise false
     */
    public boolean goCollectBag(final int bagId);

    /**
     * The {@link pt.ua.deti.entities.Porter} notifies that there are no more bags.
     */
    public void noMoreBags();

    /**
     * Reset the {@link BaggageCollectionPoint} by setting the noMoreBags has false.
     */
    public void reset();
}