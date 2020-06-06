package pt.ua.deti.shared.imp;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import pt.ua.deti.shared.stubs.ATTQInterface;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * Arrival Terminal Transfer Quay
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class ArrivalTerminalTransferQuay implements ATTQInterface {
    /** {@link Lock} used by the entities to change the internal state */
    private final Lock lock = new ReentrantLock();
    /**
     * {@link Condition} used by the {@link pt.ua.deti.entities.BusDriver} to wait
     * for all {@link pt.ua.deti.entities.Passenger}
     */
    private final Condition cBusDriver = lock.newCondition();
    /** {@link Queue} of {@link Condition} waiting for the bus */
    private final Queue<Condition> queue = new ArrayDeque<>();
    /** number of seated {@link pt.ua.deti.entities.Passenger} */
    private int seated = 0;
    /** maximum number of seats inside the bus */
    private final int numberSeats;
    /**
     * Individual synchronization point for each
     * {@link pt.ua.deti.entities.Passenger}
     */
    private final Condition pConds[];
    /** {@link GeneralRepositoryInformation} serves as log */
    private final GRIInterface gri;
    /** maximum duration that the bus driver awaits */
    private final long D;

    /**
     * Create a {@link ArrivalTerminalTransferQuay}.
     * 
     * @param N           maximum number of {@link pt.ua.deti.entities.Passenger}
     * @param numberSeats maximum number of seats
     * @param D           duration that the Bus driver awaits for passengers
     *                    (milliseconds)
     * @param gri         {@link GeneralRepositoryInformation} serves as log
     */
    public ArrivalTerminalTransferQuay(final int N, final int numberSeats, final long D, final GRIInterface gri) {
        this.numberSeats = numberSeats;
        this.D = D;
        pConds = new Condition[N];
        for (int i = 0; i < N; i++) {
            pConds[i] = lock.newCondition();
        }
        this.gri = gri;
    }

    @Override
    public void takeABus(final int id) {
        lock.lock();
        try {
            // find the condition that refers to this passenger
            Condition c = pConds[id - 1];
            queue.add(c);
            // notify the Bus driver
            gri.updateQueueAdd(id);
            cBusDriver.signal();
            // await for the signal
            c.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int announcingBusBoarding() {
        int rv = 0;
        lock.lock();
        try {
            long elapsedTime = 0;
            // wait for all the passenger
            while (queue.size() < numberSeats && TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) < D) {
                long startTime = System.nanoTime();
                cBusDriver.await(D, TimeUnit.MILLISECONDS);
                elapsedTime += System.nanoTime() - startTime;
            }
            if (queue.size() > 0) {
                // wake the top of the queue
                for (int i = 0; i < numberSeats && !queue.isEmpty(); i++) {
                    Condition c = queue.remove();
                    c.signal();
                    rv++;
                }
            } else {
                rv = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return rv;
    }

    @Override
    public void enterTheBus(final int id) {
        lock.lock();
        try {
            gri.updateQueueRemove(id);
            seated++;
            gri.updateSeatAdd(id);
            cBusDriver.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void goToDepartureTerminal(final int numberPassengers) {
        lock.lock();
        try {
            while (seated < numberPassengers) {
                cBusDriver.await();
            }
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
