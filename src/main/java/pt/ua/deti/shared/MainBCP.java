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
import pt.ua.deti.shared.imp.BaggageCollectionPoint;
import pt.ua.deti.shared.remote.GRIRemote;
import pt.ua.deti.shared.stubs.BCPInterface;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * {@link BCPInterface} Server.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class MainBCP {

    /**
     * Main class, lets make the constructor private.
     */
    private MainBCP() {
    }

    public static void main(final String[] args) {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");
        // Server port
        final int port = Integer.parseInt(prop.getProperty("bcp_port"));
        // GRI Remote
        final String gri_host = prop.getProperty("gri_host");
        final int gri_port = Integer.parseInt(prop.getProperty("gri_port"));

        // Create the Shared Region
        final GRIInterface gri = new GRIRemote(gri_host, gri_port);
        final BCPInterface bcp = new BaggageCollectionPoint(gri);

        // Stopping criteria
        final AtomicInteger done = new AtomicInteger(0);

        try {
            // Setup the server
            final ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);

            // Serve all the clients (Porter, Passenger)
            while (done.get() < 2) {
                try {
                    final Handler handler = new Handler(serverSocket.accept(), bcp, done);
                    final Thread thread = new Thread(handler);
                    thread.start();
                } catch (SocketTimeoutException e) {
                    // ignore, used to check the stopping criteria
                }
            }

            // Close the server socket
            serverSocket.close();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler that represent each client request.
     */
    private static class Handler implements Runnable {
        private final Socket socket;
        private final BCPInterface bcp;
        private final AtomicInteger done;

        /**
         * Create a new handler.
         * 
         * @param socket client socket used for communication
         * @param bcp    shared memory implementation {@link BCPInterface}
         * @param done   atomic variable used for the stopping criteria
         *               {@link AtomicInteger}
         */
        Handler(final Socket socket, final BCPInterface bcp, final AtomicInteger done) {
            this.socket = socket;
            this.bcp = bcp;
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
                    case "bcp_close":
                        // Instead of closing the log it marks the task as done
                        done.incrementAndGet();
                        reply = new MessageReply(request.type);
                        break;
                    case "bcp_reset":
                        bcp.reset();
                        reply = new MessageReply(request.type);
                        break;
                    case "bcp_noMoreBags":
                        bcp.noMoreBags();
                        reply = new MessageReply(request.type);
                        break;
                    case "bcp_goCollectBag":
                        reply = new MessageReply(request.type, bcp.goCollectBag(request.argInt0));
                        break;
                    case "bcp_storeBag":
                        bcp.storeBag(Utils.cast(request.argObj));
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