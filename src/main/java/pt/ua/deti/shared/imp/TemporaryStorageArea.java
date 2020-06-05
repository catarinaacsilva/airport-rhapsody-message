package pt.ua.deti.shared.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.ua.deti.common.Bag;
import pt.ua.deti.shared.stubs.GRIInterface;
import pt.ua.deti.shared.stubs.TSAInterface;

/**
 * Temporary Storage Area.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class TemporaryStorageArea implements TSAInterface {
    /** {@link List} of bags from the passengers */
    private final List<Bag> bags = new ArrayList<>();
    /** {@link Lock} used by the entities to change the internal state */
    private final Lock lock = new ReentrantLock();
    /** {@link GRIInterface} serves as log */
    private final GRIInterface gri;

    /**
     * Create a Temporary Storage Area.
     * 
     * @param gri {@link GRIInterface}
     */
    public TemporaryStorageArea(final GRIInterface gri) {
        this.gri = gri;
    }

    @Override
    public void storeBag(final Bag b) {
        lock.lock();
        try {
            bags.add(b);
            gri.updateStoreroom(bags.size());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        // Used for the remote version
    }
}
