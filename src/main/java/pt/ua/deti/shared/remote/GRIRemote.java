package pt.ua.deti.shared.remote;

import java.io.IOException;

import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * Remote {@link GRIInterface}.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class GRIRemote implements GRIInterface {
    /** GRI server hostname */
    private final String hostname;
    /** GRI server port */
    private final int port;

    /**
     * Creates a {@link GRIRemote}
     * 
     * @param hostname GRI server hostname
     * @param port     GRI server port
     */
    public GRIRemote(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        MessageRequest request = new MessageRequest("gri_close");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void writeHeader() {
        MessageRequest request = new MessageRequest("gri_writeHeader");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void writeLine() {
        MessageRequest request = new MessageRequest("gri_writeLine");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void writeReport() {
        MessageRequest request = new MessageRequest("gri_writeReport");
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateTRT(int inc) {
        MessageRequest request = new MessageRequest("gri_updateTRT", inc);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateFDT(int inc) {
        MessageRequest request = new MessageRequest("gri_updateFDT", inc);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateBags(int inc) {
        MessageRequest request = new MessageRequest("gri_updateBags", inc);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateMissing(int inc) {
        MessageRequest request = new MessageRequest("gri_updateMissing", inc);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updatePlane(int fn, int bn) {
        MessageRequest request = new MessageRequest("gri_updatePlane", fn, bn);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updatePlaneHold(int bn) {
        MessageRequest request = new MessageRequest("gri_updatePlaneHold", bn);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updatePStat(int pstat) {
        MessageRequest request = new MessageRequest("gri_updatePStat", pstat);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateConveyorBelt(int cb) {
        MessageRequest request = new MessageRequest("gri_updateConveyorBelt", cb);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateStoreroom(int sr) {
        MessageRequest request = new MessageRequest("gri_updateStoreroom", sr);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updatePassenger(int id, int state, int situation, int bags, int collected) {
        MessageRequest request = new MessageRequest("gri_updatePassenger", id, state, situation, bags, collected);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateQueueAdd(int id) {
        MessageRequest request = new MessageRequest("gri_updateQueueAdd", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateQueueRemove(int id) {
        MessageRequest request = new MessageRequest("gri_updateQueueRemove", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateSeatAdd(int id) {
        MessageRequest request = new MessageRequest("gri_updateSeatAdd", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateSeatRemove(int id) {
        MessageRequest request = new MessageRequest("gri_updateSeatRemove", id);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void updateBusDriver(int bstat) {
        MessageRequest request = new MessageRequest("gri_updateBusDriver", bstat);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }

    @Override
    public void debug(String msg) {
        MessageRequest request = new MessageRequest("gri_debug", msg);
        MessageReply reply = Utils.remoteMethod(hostname, port, request);
        if (reply.retCode != 0) {
            System.err.println("Error: " + Utils.cast(reply.retObj));
        }
    }
}