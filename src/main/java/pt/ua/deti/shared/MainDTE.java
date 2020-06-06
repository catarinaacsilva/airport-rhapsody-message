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
import pt.ua.deti.shared.imp.DepartureTerminalEntrance;
import pt.ua.deti.shared.remote.ATERemote;
import pt.ua.deti.shared.stubs.ATEInterface;
import pt.ua.deti.shared.stubs.DTEInterface;

/**
 * {@link DTEInterface} Server.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MainDTE {

    /**
     * Main class, lets make the constructor private.
     */
    private MainDTE() {
    }

    public static void main(final String[] args) {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");
        // The number of Passengers per Plane
        final int N = Integer.parseInt(prop.getProperty("N"));
        // ATE Remote
        final String ate_host = prop.getProperty("ate_host");
        final int ate_port = Integer.parseInt(prop.getProperty("ate_port"));
        // Server port
        final int port = Integer.parseInt(prop.getProperty("dte_port"));

        // Create the Shared Region
        final ATEInterface ate = new ATERemote(ate_host, ate_port);
        final DTEInterface dte = new DepartureTerminalEntrance(N, ate);

        // Stopping criteria
        final AtomicInteger done = new AtomicInteger(0);

        try {
            // Setup the server
            final ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);

            // Serve all the clients (Bus Driver, Passenger)
            while (done.get() < 2) {
                try {
                    final Handler handler = new Handler(serverSocket.accept(), dte, done);
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
        private final DTEInterface dte;
        private final AtomicInteger done;

        /**
         * Create a new handler.
         * 
         * @param socket client socket used for communication
         * @param dte    shared memory implementation {@link DTEInterface}
         * @param done   atomic variable used for the stopping criteria
         *               {@link AtomicInteger}
         */
        Handler(final Socket socket, final DTEInterface dte, final AtomicInteger done) {
            this.socket = socket;
            this.dte = dte;
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
                    case "dte_close":
                        // Instead of closing the log it marks the task as done
                        done.incrementAndGet();
                        reply = new MessageReply(request.type);
                        break;
                    case "dte_reset":
                        dte.reset();
                        reply = new MessageReply(request.type);
                        break;
                    case "dte_prepareNextLeg":
                        dte.prepareNextLeg(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "dte_getBlocked":
                        reply = new MessageReply(request.type, 0, dte.getBlocked());
                        break;
                    default:
                        reply = new MessageReply(request.type, -1, "unknown method: " + request.type);
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