package pt.ua.deti.shared.imp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
 * @author Duarte Dias
 * @version 1.0
 */
public class DepartureTerminalEntrance {
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
    /** {@link ArrivalTerminalExit} */
    private ArrivalTerminalExit ate;

    /**
     * Create a {@link DepartureTerminalEntrance}.
     * 
     * @param total total number of {@link pt.ua.deti.entities.Passenger}
     */
    public DepartureTerminalEntrance(final int total) {
        this.total = total;
    }

    /**
     * Define the other exit point (@{link ArrivalTerminalExit}).
     * 
     * @param ate the other exit point (@{link ArrivalTerminalExit})
     */
    public void setATE(final ArrivalTerminalExit ate) {
        this.ate = ate;
    }

    /**
     * Reset the {@link DepartureTerminalEntrance} by setting the blocked to 0.
     */
    public void reset() {
        lock.lock();
        try {
            blocked.set(0);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Prepare next leg.
     * 
     * @param id the identification of the Passenger
     */
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

    /**
     * Returns the number of blocked {@link pt.ua.deti.entities.Passenger}.
     * 
     * @return the number of blocked {@link pt.ua.deti.entities.Passenger}
     */
    public int getBlocked() {
        return blocked.get();
    }
}