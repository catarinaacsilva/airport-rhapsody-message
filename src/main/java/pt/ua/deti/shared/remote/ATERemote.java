package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.ATEInterface;

/**
 * Remote {@link ATEInterface}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class ATERemote implements ATEInterface {
    /** ATE server hostname */
    private final String hostname;
    /** ATE server port */
    private final int port;

    /**
     * Creates a {@link BRORemote}
     * 
     * @param hostname BRO server hostname
     * @param port     BRO server port
     */
    public ATERemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("ate_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void reset(boolean last) {
        MessageRequest request = new MessageRequest("ate_reset", last);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void goHome(int id) {
        MessageRequest request = new MessageRequest("ate_goHome", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public int getBlocked() {
        MessageRequest request = new MessageRequest("ate_getBlocked");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        int rv = 0;
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        } else {
            rv = reply.retInt;
        }
        return rv;
    }

    @Override
    public boolean hasDaysWorkEnded() {
        MessageRequest request = new MessageRequest("ate_hasDaysWorkEnded");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        boolean rv = false;
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        } else {
            rv = reply.retBool;
        }
        return rv;
    }

}