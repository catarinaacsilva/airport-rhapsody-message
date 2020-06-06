package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.DTTQInterface;

/**
 * Remote {@link DTTQInterface}.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class DTTQRemote implements DTTQInterface {
    /** DTTQ server hostname */
    private final String hostname;
    /** DTTQ server port */
    private final int port;

    /**
     * Creates a {@link DTTQRemote}
     * 
     * @param hostname DTTQ server hostname
     * @param port     DTTQ server port
     */
    public DTTQRemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("dttq_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void leaveTheBus(int id) {
        MessageRequest request = new MessageRequest("dttq_leaveTheBus", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void parkTheBusAndLetPassOff(int numberPassengers) {
        MessageRequest request = new MessageRequest("dttq_parkTheBusAndLetPassOff", numberPassengers);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }
}