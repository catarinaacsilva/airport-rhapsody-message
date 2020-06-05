package pt.ua.deti.shared.imp;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Arrival Lounge location.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class ArrivalLounge {
    /** {@link Lock} used by the entities to change the internal state */
    private final Lock lock = new ReentrantLock();
    /** {@link Condition} used by the {@link pt.ua.deti.entities.Porter} to wait for all {@link pt.ua.deti.entities.Passenger} */
    private final Condition cond = lock.newCondition();
    /** The number of {@link pt.ua.deti.entities.Passenger} that have disembarked */
    private int disembark;
    /** The total number of {@link pt.ua.deti.entities.Passenger} */
    private int totalPassengers;
    /** Flag used to notify the {@link pt.ua.deti.entities.Porter} if it is a new plane */
    private boolean newPlane;

    /**
     * Creates a {@link ArrivalLounge}
     * @param totalPassengers the number of {@link pt.ua.deti.entities.Passenger} in
     *                        the {@link pt.ua.deti.common.Plane}
     */
    public ArrivalLounge(final int totalPassengers) {
        disembark = 0;
        newPlane = true;
        this.totalPassengers = totalPassengers;
    }

    /**
     * Reset the {@link ArrivalLounge} by setting the disembark at zero.
     */
    public void reset() {
        lock.lock();
        try {
            disembark = 0;
            newPlane = true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The {@link pt.ua.deti.entities.Porter} takes a rest and waits for all the {@link pt.ua.deti.entities.Passenger} to
     * leave the {@link pt.ua.deti.common.Plane}.
     */
    public void takeARest() {
        lock.lock();
        try {
            while (disembark < totalPassengers || !newPlane) {
                cond.await();
            }
            newPlane = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The {@link pt.ua.deti.entities.Passenger} indicates that has left the {@link pt.ua.deti.common.Plane}.
     */
    public void whatShouldIDo() {
        lock.lock();
        try {
            disembark++;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }
}
