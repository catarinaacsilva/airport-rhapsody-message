package pt.ua.deti.shared.stubs;

import java.io.Closeable;

import pt.ua.deti.common.Bag;

/**
 * Temporary Storage Area Interface.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public interface TSAInterface extends Closeable {

    /**
     * Method used by the porter to store bags in the Temporary Storage Area.
     * 
     * @param b {@link pt.ua.deti.common.Bag} carry be the
     *          {@link pt.ua.deti.entities.Porter}
     */
    public void storeBag(final Bag b);
}