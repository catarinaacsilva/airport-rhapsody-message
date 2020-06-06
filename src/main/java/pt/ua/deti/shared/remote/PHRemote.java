package pt.ua.deti.shared.remote;

import java.io.IOException;
import java.util.List;

import pt.ua.deti.common.Bag;
import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.PHInterface;

/**
 * Remote {@link PHInterface}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class PHRemote implements PHInterface {
    /** PH server hostname */
    private final String hostname;
    /** PH server port */
    private final int port;

    /**
     * Creates a {@link PHRemote}
     * 
     * @param hostname PH server hostname
     * @param port     PH server port
     */
    public PHRemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void loadBags(List<Bag> bags, boolean lastPlane) {
        MessageRequest request = new MessageRequest("ph_loadBags", lastPlane, bags);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public Bag getBag() {
        MessageRequest request = new MessageRequest("ph_getBag");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        Bag bag = null;
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        } else {
            bag = Utils.cast(reply.retObj);
        }
        return bag;
    }

    @Override
    public boolean hasBags() {
        MessageRequest request = new MessageRequest("ph_hasBags");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        boolean rv = false;
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        } else {
            rv = reply.retBool;
        }
        return rv;
    }

    @Override
    public boolean lastPlane() {
        MessageRequest request = new MessageRequest("ph_lastPlane");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        boolean rv = false;
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        } else {
            rv = reply.retBool;
        }
        return rv;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("ph_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }
}