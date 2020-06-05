package pt.ua.deti.shared;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import pt.ua.deti.common.MessageReply;
import pt.ua.deti.common.MessageRequest;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.imp.GeneralRepositoryInformation;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * {@link GeneralRepositoryInformation} Server.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MainGRI {
    public static void main(final String[] args) {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");
        // The path to the logging file
        final String filename = prop.getProperty("L");
        // Flag that outputs the logger information on the terminal
        final boolean verbose = Boolean.parseBoolean(prop.getProperty("V"));
        // Server port
        final int port = Integer.parseInt(prop.getProperty("gri_port"));

        // Create the Shared Region
        final GRIInterface gri = new GeneralRepositoryInformation(filename, verbose);

        // Stopping criteria
        final AtomicInteger done = new AtomicInteger(0);

        try {
            // Setup the server
            final ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);

            // Write initial line of the log
            gri.writeHeader();

            // Serve all the clients (Porter, Bus Driver, Passengers)
            while (done.get() < 3) {
                try {
                    final ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), gri, done);
                    final Thread thread = new Thread(clientHandler);
                    thread.start();
                } catch(SocketTimeoutException e) {
                    //ignore, used to check the stopping criteria
                }
            }

            // Close the server socket
            serverSocket.close();

            // Write the final report and close the logger file
            gri.writeReport();
            gri.close();
            
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final GRIInterface gri;
        private final AtomicInteger done;

        /**
         * 
         * @param socket
         */
        ClientHandler(final Socket socket, final GRIInterface gri, final AtomicInteger done) {
            this.socket = socket;
            this.gri = gri;
            this.done = done;
        }

        @Override
        public void run() {
            try {
                // create input buffer and output buffer
                final ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                final ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

                // wait for input from client and send response back to client
                final MessageRequest request = Utils.cast(is.readObject());
                MessageReply reply = null;

                // Check the message type and execute the corresponding method
                switch (request.type) {
                    case "gri_close":
                        // gri.close();
                        // Instead of closing the log it marks the task as done
                        done.incrementAndGet();
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_writeHeader":
                        gri.writeHeader();
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_writeLine":
                        gri.writeLine();
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_writeReport":
                        gri.writeReport();
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateTRT":
                        gri.updateTRT(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateFDT":
                        gri.updateFDT(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateBags":
                        gri.updateBags(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateMissing":
                        gri.updateMissing(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updatePlane":
                        gri.updatePlane(request.argInt0, request.argInt1);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updatePlaneHold":
                        gri.updatePlaneHold(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updatePStat":
                        gri.updatePStat(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateConveyorBelt":
                        gri.updateConveyorBelt(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateStoreroom":
                        gri.updateStoreroom(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updatePassenger":
                        gri.updatePassenger(request.argInt0, request.argInt1, request.argInt2, request.argInt3,
                                request.argInt4);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateQueueAdd":
                        gri.updateQueueAdd(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateQueueRemove":
                        gri.updateQueueRemove(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateSeatAdd":
                        gri.updateSeatAdd(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateSeatRemove":
                        gri.updateSeatRemove(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_updateBusDriver":
                        gri.updateBusDriver(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "gri_debug":
                        gri.debug(Utils.cast(request.argObj));
                        reply = new MessageReply(request.type);
                        break;
                    default:
                        reply = new MessageReply(request.type, -1, "unknown method");
                        break;
                }

                // send the reply back to the client
                os.writeObject(reply);

                // close all streams and sockets
                os.close();
                is.close();
                socket.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}