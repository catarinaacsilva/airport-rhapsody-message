package pt.ua.deti.shared.stubs;

import java.io.Closeable;

/**
 * Baggage Reclaim Office Interface.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public interface BROInterface extends Closeable {
    /**
     * Report missing bag.
     * 
     * @param bagId the id of the {@link pt.ua.deti.common.Bag}
     */
    public void reportMissingBag(final int bagId);
}