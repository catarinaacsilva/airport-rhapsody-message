package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.DTEInterface;

/**
 * Remote {@link DTEInterface}.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class DTERemote implements DTEInterface {
    /** DTE server hostname */
    private final String hostname;
    /** DTE server port */
    private final int port;

    /**
     * Creates a {@link BRORemote}
     * 
     * @param hostname BRO server hostname
     * @param port     BRO server port
     */
    public DTERemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("dte_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void reset() {
        MessageRequest request = new MessageRequest("dte_reset");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void prepareNextLeg(int id) {
        MessageRequest request = new MessageRequest("dte_prepareNextLeg", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public int getBlocked() {
        MessageRequest request = new MessageRequest("dte_getBlocked");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        int rv = 0;
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        } else {
            rv = reply.retInt;
        }
        return rv;
    }
    
}