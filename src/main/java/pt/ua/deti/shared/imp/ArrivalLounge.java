package pt.ua.deti.shared.imp;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.ua.deti.shared.stubs.ALInterface;

/**
 * Arrival Lounge location.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class ArrivalLounge implements ALInterface {
    /** {@link Lock} used by the entities to change the internal state */
    private final Lock lock = new ReentrantLock();
    /**
     * {@link Condition} used by the {@link pt.ua.deti.entities.Porter} to wait for
     * all {@link pt.ua.deti.entities.Passenger}
     */
    private final Condition cond = lock.newCondition();
    /** The number of {@link pt.ua.deti.entities.Passenger} that have disembarked */
    private int disembark;
    /** The total number of {@link pt.ua.deti.entities.Passenger} */
    private int totalPassengers;
    /**
     * Flag used to notify the {@link pt.ua.deti.entities.Porter} if it is a new
     * plane
     */
    private boolean newPlane;

    /**
     * Creates a {@link ArrivalLounge}
     * 
     * @param totalPassengers the number of {@link pt.ua.deti.entities.Passenger} in
     *                        the {@link pt.ua.deti.common.Plane}
     */
    public ArrivalLounge(final int totalPassengers) {
        disembark = 0;
        newPlane = true;
        this.totalPassengers = totalPassengers;
    }

    @Override
    public void reset() {
        lock.lock();
        try {
            disembark = 0;
            newPlane = true;
        } finally {
            lock.unlock();
        }
    }

    @Override
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

    @Override
    public void whatShouldIDo() {
        lock.lock();
        try {
            disembark++;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        // Used for the remote version
    }
}
