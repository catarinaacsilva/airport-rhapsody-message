package pt.ua.deti.shared.imp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Arrival Terminal Exit.
 * 
 * <p>
 * This shared region synchronizes with the {@link DepartureTerminalEntrance}
 * (the other exit point). The strategic adopted was the following:
 * </p>
 * <ul>
 * <li>The {@link #goHome(int)} request the blocked
 * {@link pt.ua.deti.entities.Passenger} on the other region
 * <li>The blocked counts are implemented with {@link AtomicInteger} in order to
 * avoid deadlock
 * <li>Also, there is no signalization between the shared regions
 * <li>The {@link #goHome(int)} awaits on the {@link Condition} for a certain
 * time and awakes by itself
 * </ul>
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class ArrivalTerminalExit {
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
    /** {@link DepartureTerminalEntrance} */
    private DepartureTerminalEntrance dte;
    /** Flag used to identify if is the last plane */
    private boolean last = false;

    /**
     * Create a {@link ArrivalTerminalExit}
     * 
     * @param total total number of {@link pt.ua.deti.entities.Passenger}
     */
    public ArrivalTerminalExit(final int total) {
        this.total = total;
    }

    /**
     * Define the other exit point (@{link DepartureTerminalEntrance}).
     * 
     * @param dte the other exit point (@{link DepartureTerminalEntrance})
     */
    public void setDTE(final DepartureTerminalEntrance dte) {
        this.dte = dte;
    }

    /**
     * Reset the {@link DepartureTerminalEntrance} by setting the blocked to 0.
     * 
     * @param last flag used to identify if is the last plane
     */
    public void reset(final boolean last) {
        lock.lock();
        try {
            blocked.set(0);
            this.last = last;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Go Home.
     * 
     * @param id the identification of the Passenger
     */
    public void goHome(final int id) {
        lock.lock();
        try {
            blocked.incrementAndGet();
            while ((dte.getBlocked() + blocked.get()) < total) {
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

    /**
     * Has days work ended.
     * 
     * @return true if the simulation is finished; otherwise false
     */
    public boolean hasDaysWorkEnded() {
        boolean rv = false;
        lock.lock();
        try {
            rv = ((dte.getBlocked() + blocked.get()) == total) && last;
        } finally {
            lock.unlock();
        }
        return rv;
    }
}
