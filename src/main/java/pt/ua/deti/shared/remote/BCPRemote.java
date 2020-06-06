package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.Bag;
import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.BCPInterface;

/**
 * Remote {@link BCPRemote}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class BCPRemote implements BCPInterface {
    /** BCP server hostname */
    private final String hostname;
    /** BCP server port */
    private final int port;

    /**
     * Creates a {@link BCPRemote}
     * 
     * @param hostname BCP server hostname
     * @param port     BCP server port
     */
    public BCPRemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("bcp_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void storeBag(Bag bag) {
        MessageRequest request = new MessageRequest("bcp_storeBag", bag);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public boolean goCollectBag(int bagId) {
        MessageRequest request = new MessageRequest("bcp_goCollectBag", bagId);
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
    public void noMoreBags() {
        MessageRequest request = new MessageRequest("bcp_noMoreBags");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void reset() {
        MessageRequest request = new MessageRequest("bcp_reset");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }
    
}