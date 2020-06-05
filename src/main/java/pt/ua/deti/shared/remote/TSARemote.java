package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.Bag;
import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.TSAInterface;

/**
 * Remote {@link TSAInterface}.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class TSARemote implements TSAInterface {
    /** TSA server hostname */
    private final String hostname;
    /** TSA server port */
    private final int port;

    /**
     * Creates a {@link TSARemote}
     * 
     * @param hostname TSA server hostname
     * @param port     TSA server port
     */
    public TSARemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("tsa_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void storeBag(Bag bag) {
        MessageRequest request = new MessageRequest("tsa_storeBag", bag);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }
}