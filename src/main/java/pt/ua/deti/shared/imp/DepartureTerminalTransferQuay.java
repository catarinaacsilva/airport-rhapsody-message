package pt.ua.deti.shared.imp;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.ua.deti.shared.stubs.DTTQInterface;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * Departure Terminal Transfer Quay.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class DepartureTerminalTransferQuay implements DTTQInterface {
    /** {@link Lock} used by the entities to change the internal state */
    private final Lock lock = new ReentrantLock();
    /**
     * {@link Condition} used by the {@link pt.ua.deti.entities.Passenger} to wait
     * for the {@link pt.ua.deti.entities.BusDriver}
     */
    private final Condition pCond = lock.newCondition();
    /**
     * {@link Condition} used by the {@link pt.ua.deti.entities.BusDriver} to wait
     * for all {@link pt.ua.deti.entities.Passenger}
     */
    private final Condition bCond = lock.newCondition();
    /** {@link GRIInterface} serves as log */
    private final GRIInterface gri;
    /** Flag that indicates if the Bus has arrived */
    private boolean arrived = false;
    /** Count how many {@link pt.ua.deti.entities.Passenger} left the bus */
    private int left = 0;

    /**
     * Create a {@link DepartureTerminalTransferQuay}.
     * 
     * @param gri {@link GRIInterface} serves as log
     */
    public DepartureTerminalTransferQuay(final GRIInterface gri) {
        this.gri = gri;
    }

    @Override
    public void leaveTheBus(final int id) {

        lock.lock();
        try {
            while (!arrived) {
                pCond.await();
            }
            left += 1;
            bCond.signal();
            gri.updateSeatRemove(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void parkTheBusAndLetPassOff(final int numberPassengers) {
        lock.lock();
        try {
            arrived = true;
            pCond.signalAll();
            while (left < numberPassengers) {
                bCond.await();
            }
            left = 0;
            arrived = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        // Used for the remote version
    }
}
