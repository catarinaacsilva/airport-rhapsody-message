package pt.ua.deti.shared.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Baggage Reclaim Office.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class BaggageReclaimOffice {
    /** {@link Lock} used by the entities to change the internal {@link List} */
    private final Lock lock = new ReentrantLock();
    /** {@link List} ids of the missing bags */
    private final List<Integer> bagIds;
    
    /** Create a instance of Baggage Reclaim Office */
    public BaggageReclaimOffice(){
        this.bagIds = new ArrayList<>();
    }

    /**
     * Report missing bag.
     * @param bagId the id of the {@link pt.ua.deti.common.Bag}
     */
    public void reportMissingBag(final int bagId){
        lock.lock();
        try {
            // store the id from a missing bag
            bagIds.add(bagId);
        }finally {
            lock.unlock();
        }
    }
}
