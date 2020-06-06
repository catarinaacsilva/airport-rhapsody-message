package pt.ua.deti.shared.stubs;

import java.io.Closeable;
import java.util.List;

import pt.ua.deti.common.Bag;

/**
 * Plane Hold Interface.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public interface PHInterface extends Closeable {

    /**
     * Unload bags from the {@link pt.ua.deti.common.Plane} and place them into the
     * {@link PHInterface}.
     * 
     * @param bags      {@link List} of {@link pt.ua.deti.common.Bag}
     * @param lastPlane flag used to identify if this is the last plane
     */
    public void loadBags(final List<Bag> bags, final boolean lastPlane);

    /**
     * Removes and returns one {@link pt.ua.deti.common.Bag} from this location
     * 
     * @return {@link pt.ua.deti.common.Bag}
     */
    public Bag getBag();

    /**
     * Returns true is there are bags to collect; otherwise false.
     * 
     * @return true is there are bags to collect; otherwise false
     */
    public boolean hasBags();

    /**
     * Returns true of this is the last plane; otherwise false.
     * 
     * @return true of this is the last plane; otherwise false
     */
    public boolean lastPlane();
}