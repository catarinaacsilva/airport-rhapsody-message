package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.BROInterface;

/**
 * Remote {@link BRORemote}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class BRORemote implements BROInterface {
    /** BRO server hostname */
    private final String hostname;
    /** BRO server port */
    private final int port;

    /**
     * Creates a {@link BRORemote}
     * 
     * @param hostname BRO server hostname
     * @param port     BRO server port
     */
    public BRORemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("bro_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void reportMissingBag(int bagId) {
        MessageRequest request = new MessageRequest("bro_reportMissingBag", bagId);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);

        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

}