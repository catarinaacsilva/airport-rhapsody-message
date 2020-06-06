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
import pt.ua.deti.shared.imp.ArrivalTerminalExit;
import pt.ua.deti.shared.remote.DTERemote;
import pt.ua.deti.shared.stubs.ATEInterface;
import pt.ua.deti.shared.stubs.DTEInterface;

/**
 * {@link ATEInterface} Server.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class MainATE {

    /**
     * Main class, lets make the constructor private.
     */
    private MainATE() {
    }

    public static void main(final String[] args) {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");
        // The number of Passengers per Plane
        final int N = Integer.parseInt(prop.getProperty("N"));
        // DTE Remote
        final String dte_host = prop.getProperty("dte_host");
        final int dte_port = Integer.parseInt(prop.getProperty("dte_port"));
        // Server port
        final int port = Integer.parseInt(prop.getProperty("ate_port"));

        // Create the Shared Region
        final DTEInterface dte = new DTERemote(dte_host, dte_port);
        final ATEInterface ate = new ArrivalTerminalExit(N, dte);

        // Stopping criteria
        final AtomicInteger done = new AtomicInteger(0);

        try {
            // Setup the server
            final ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);

            // Serve all the clients (Bus Driver, Passenger)
            while (done.get() < 2) {
                try {
                    final Handler handler = new Handler(serverSocket.accept(), ate, done);
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
        private final ATEInterface ate;
        private final AtomicInteger done;

        /**
         * Create a new handler.
         * 
         * @param socket client socket used for communication
         * @param ate    shared memory implementation {@link ATEInterface}
         * @param done   atomic variable used for the stopping criteria
         *               {@link AtomicInteger}
         */
        Handler(final Socket socket, final ATEInterface ate, final AtomicInteger done) {
            this.socket = socket;
            this.ate = ate;
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
                    case "ate_close":
                        // Instead of closing the log it marks the task as done
                        done.incrementAndGet();
                        reply = new MessageReply(request.type);
                        break;
                    case "ate_reset":
                        ate.reset(request.argBool);
                        reply = new MessageReply(request.type);
                        break;
                    case "ate_goHome":
                        ate.goHome(request.argInt0);
                        reply = new MessageReply(request.type);
                        break;
                    case "ate_getBlocked":
                        reply = new MessageReply(request.type, 0, ate.getBlocked());
                        break;
                    case "ate_hasDaysWorkEnded":
                        reply = new MessageReply(request.type, ate.hasDaysWorkEnded());
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