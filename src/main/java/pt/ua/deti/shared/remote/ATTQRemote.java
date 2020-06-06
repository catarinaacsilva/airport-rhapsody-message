package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.ATTQInterface;

/**
 * Remote {@link ATTQInterface}.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class ATTQRemote implements ATTQInterface {
    /** ATTQ server hostname */
    private final String hostname;
    /** ATTQ server port */
    private final int port;

    /**
     * Creates a {@link ATTQRemote}
     * 
     * @param hostname ATTQ server hostname
     * @param port     ATTQ server port
     */
    public ATTQRemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void takeABus(int id) {
        MessageRequest request = new MessageRequest("attq_takeABus", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public int announcingBusBoarding() {
        MessageRequest request = new MessageRequest("attq_announcingBusBoarding");
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
    public void enterTheBus(int id) {
        MessageRequest request = new MessageRequest("attq_enterTheBus", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void goToDepartureTerminal(int numberPassengers) {
        MessageRequest request = new MessageRequest("attq_goToDepartureTerminal", numberPassengers);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("attq_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

}