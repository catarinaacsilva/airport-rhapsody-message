package pt.ua.deti.shared.imp;

import java.io.IOException;
import java.util.List;

import pt.ua.deti.common.Bag;
import pt.ua.deti.shared.stubs.GRIInterface;
import pt.ua.deti.shared.stubs.PHInterface;

/**
 * Plane Hold Interface.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class PlaneHold implements PHInterface {
    /** {@link List} of the bags of the passengers */
    private List<Bag> bags;
    /** {@link GRIInterface} serves as log */
    private final GRIInterface gri;
    /** Flag used to identify if this is the last plane */
    private boolean lastPlane = false;

    /**
     * Create a Plane Hold location.
     * 
     * @param gri {@link GRIInterface}
     */
    public PlaneHold(final GRIInterface gri) {
        this.bags = null;
        this.gri = gri;
    }

    @Override
    public synchronized void loadBags(final List<Bag> bags, final boolean lastPlane) {
        this.bags = bags;
        this.lastPlane = lastPlane;
    }

    @Override
    public synchronized Bag getBag() {
        if (bags.size() > 0) {
            Bag b = bags.get(0);
            bags.remove(0);
            gri.updatePlaneHold(bags.size());
            return b;
        } else {
            return null;
        }
    }

    @Override
    public synchronized boolean hasBags() {
        if (bags == null) {
            return false;
        } else if (bags.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public synchronized boolean lastPlane() {
        return lastPlane;
    }

    @Override
    public void close() throws IOException {
        // Used for the remote version
    }
}