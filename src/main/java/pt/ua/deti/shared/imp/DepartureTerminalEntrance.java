package pt.ua.deti.shared.imp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.ua.deti.shared.stubs.ATEInterface;
import pt.ua.deti.shared.stubs.DTEInterface;

/**
 * Departure Terminal Entrance.
 * 
 * <p>
 * This shared region synchronizes with the {@link ArrivalTerminalExit} (the
 * other exit point). The strategic adopted was the following:
 * </p>
 * <ul>
 * <li>The {@link #prepareNextLeg(int)} request the blocked
 * {@link pt.ua.deti.entities.Passenger} on the other region
 * <li>The blocked counts are implemented with {@link AtomicInteger} in order to
 * avoid deadlock
 * <li>Also, there is no signalization between the shared regions
 * <li>The {@link #prepareNextLeg(int)} awaits on the {@link Condition} for a
 * certain time and awakes by itself
 * </ul>
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class DepartureTerminalEntrance implements DTEInterface {
    /** {@link Lock} used by the entities to change the internal state */
    private final Lock lock = new ReentrantLock();
    /** {@link Condition} used by the passenger to check again for bags */
    private final Condition cond = lock.newCondition();
    /**
     * {@link AtomicInteger} used to count the blocked
     * {@link pt.ua.deti.entities.Passenger}
     */
    private final AtomicInteger blocked = new AtomicInteger(0);
    /** Total number of {@link pt.ua.deti.entities.Passenger} */
    private final int total;
    /** {@link ATEInterface} */
    private ATEInterface ate;

    /**
     * Create a {@link DepartureTerminalEntrance}.
     * 
     * @param total total number of {@link pt.ua.deti.entities.Passenger}
     */
    public DepartureTerminalEntrance(final int total) {
        this.total = total;
        this.ate = null;
    }

    /**
     * Create a {@link DepartureTerminalEntrance}.
     * 
     * @param total total number of {@link pt.ua.deti.entities.Passenger}
     * @param ate the other exit point (@{link ATEInterface})
     */
    public DepartureTerminalEntrance(final int total, ATEInterface ate) {
        this.total = total;
        this.ate = ate;
    }

    /**
     * Define the other exit point (@{link ATEInterface}).
     * 
     * @param ate the other exit point (@{link ATEInterface})
     */
    public void setATE(final ATEInterface ate) {
        this.ate = ate;
    }

    @Override
    public void reset() {
        lock.lock();
        try {
            blocked.set(0);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void prepareNextLeg(final int id) {
        lock.lock();
        try {
            blocked.incrementAndGet();
            while ((ate.getBlocked() + blocked.get()) < total) {
                cond.await(300, TimeUnit.MILLISECONDS);
            }
            cond.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getBlocked() {
        return blocked.get();
    }

    @Override
    public void close() throws IOException {
        // Used for the remote version
    }
}