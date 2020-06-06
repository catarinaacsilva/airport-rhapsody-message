package pt.ua.deti.shared.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.ua.deti.common.Bag;
import pt.ua.deti.shared.stubs.BCPInterface;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * Baggage Collection Point.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class BaggageCollectionPoint implements BCPInterface {
    /** {@link List} of bags from the passengers */
    private final List<Bag> bags = new ArrayList<>();
    /** {@link Lock} used by the entities to change the internal state */
    private final Lock lock = new ReentrantLock();
    /** {@link Condition} used by the passenger to check again for bags */
    private final Condition cond = lock.newCondition();
    /**
     * Boolean that indicates that there are no more bags on the {@link PlaneHold}
     */
    private boolean noMoreBags = false;
    /** {@link GRIInterface} serves as log */
    private final GRIInterface gri;

    /**
     * Create a Baggage Collection Point.
     * 
     * @param gri {@link GRIInterface}
     */
    public BaggageCollectionPoint(final GRIInterface gri) {
        this.gri = gri;
    }

    @Override
    public void storeBag(final Bag b) {
        lock.lock();
        try {
            bags.add(b);
            gri.updateConveyorBelt(bags.size());
            cond.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean goCollectBag(final int bagId) {
        lock.lock();
        boolean found = false, done = false;
        try {
            while (!done) {
                // check if the bag is in the conveyor belt
                // the bag is the same if the ids mathc
                for (Bag b : bags) {
                    if (b.id() == bagId) {
                        found = true;
                        done = true;
                    }
                }
                // If the bag is not found, wait...
                if (!found && !noMoreBags) {
                    cond.await();
                } else {
                    done = true;
                }
            }
            gri.updateConveyorBelt(bags.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return found;
    }

    @Override
    public void noMoreBags() {
        lock.lock();
        try {
            noMoreBags = true;
            cond.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void reset() {
        lock.lock();
        try {
            noMoreBags = false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        // Used for the remote version
    }
}