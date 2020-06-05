package pt.ua.deti.entities;

import pt.ua.deti.common.Bag;
import pt.ua.deti.shared.imp.ArrivalLounge;
import pt.ua.deti.shared.imp.BaggageCollectionPoint;
import pt.ua.deti.shared.imp.TemporaryStorageArea;
import pt.ua.deti.shared.stubs.GRIInterface;
import pt.ua.deti.shared.stubs.PHInterface;

/**
 * Porter entity.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class Porter implements Runnable {
    /**
     * States that describe the life cycle of a {@link pt.ua.deti.entities.Porter}
     */
    protected static enum State {
        WAITING_FOR_A_PLANE_TO_LAND, AT_THE_PLANES_HOLD, AT_THE_LUGGAGE_BELT_CONVEYOR, AT_THE_STOREROOM
    }

    /** {@link State} the state of the {@link pt.ua.deti.entities.Porter} */
    private State state;
    /** {@link GeneralRepositoryInformation} serves as log */
    private final GRIInterface gri;
    /** {@link ArrivalLounge} */
    private final ArrivalLounge al;
    /** {@link PHInterface} */
    private final PHInterface ph;
    /** {@link BaggageCollectionPoint} */
    private final BaggageCollectionPoint bcp;
    /** {@link TemporaryStorageArea} */
    private final TemporaryStorageArea tsa;
    /** Temporary storage to move {@link pt.ua.deti.common.Bag} */
    private Bag temp = null;
    /** Flag used to indicate if the life cycle is done */
    private boolean done = false;

    /**
     * Creates a new {@link pt.ua.deti.entities.Porter},
     * 
     * @param al  {@link ArrivalLounge}
     * @param ph  {@link PHInterface}
     * @param bcp {@link BaggageCollectionPoint}
     * @param tsa {@link TemporaryStorageArea}
     * @param gri {@link GeneralRepositoryInformation}
     */
    public Porter(final ArrivalLounge al, final PHInterface ph, final BaggageCollectionPoint bcp,
            final TemporaryStorageArea tsa, final GRIInterface gri) {
        this.state = State.WAITING_FOR_A_PLANE_TO_LAND;
        this.al = al;
        this.ph = ph;
        this.bcp = bcp;
        this.tsa = tsa;
        this.gri = gri;
    }

    @Override
    public void run() {
        while (!done) {
            switch (state) {
                case WAITING_FOR_A_PLANE_TO_LAND:
                    takeARest();
                    tryToCollectABag();
                    break;
                case AT_THE_PLANES_HOLD:
                    if (temp != null) {
                        carryItToAppropriateStore();
                    } else {
                        noMoreBagsToCollect();
                    }
                    break;
                case AT_THE_LUGGAGE_BELT_CONVEYOR:
                    tryToCollectABag();
                    break;
                case AT_THE_STOREROOM:
                    tryToCollectABag();
                    break;
                default:
                    break;
            }
        }

        try {
            gri.close();
            ph.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Take a rest.
     */
    private void takeARest() {
        state = State.WAITING_FOR_A_PLANE_TO_LAND;
        gri.updatePStat(state.ordinal());
        al.takeARest();
    }

    /**
     * Try to collect a {@link pt.ua.deti.common.Bag}.
     */
    private void tryToCollectABag() {
        state = State.AT_THE_PLANES_HOLD;
        gri.updatePStat(state.ordinal());
        temp = ph.getBag();

        gri.updatePStat(state.ordinal());
    }

    /**
     * No more {@link pt.ua.deti.common.Bag}s to collect.
     */
    private void noMoreBagsToCollect() {
        state = State.WAITING_FOR_A_PLANE_TO_LAND;
        gri.updatePStat(state.ordinal());
        bcp.noMoreBags();
        if (ph.lastPlane()) {
            done = true;
        }
    }

    /**
     * Carry it to the Appropriate Store.
     */
    private void carryItToAppropriateStore() {
        if (temp.transit()) {
            state = State.AT_THE_STOREROOM;
            gri.updatePStat(state.ordinal());
            tsa.storeBag(temp);
        } else {
            state = State.AT_THE_LUGGAGE_BELT_CONVEYOR;
            gri.updatePStat(state.ordinal());
            bcp.storeBag(temp);
        }
        temp = null;
    }
}
