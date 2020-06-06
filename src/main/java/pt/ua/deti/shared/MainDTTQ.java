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
import pt.ua.deti.shared.imp.DepartureTerminalTransferQuay;
import pt.ua.deti.shared.remote.GRIRemote;
import pt.ua.deti.shared.stubs.DTTQInterface;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * {@link DepartureTerminalTransferQuay} Server.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MainDTTQ {

    /**
     * Main class, lets make the constructor private.
     */
    private MainDTTQ() {
    }

    public static void main(final String[] args) {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");
        // Server port
        final int port = Integer.parseInt(prop.getProperty("dttq_port"));
        // GRI Remote
        final String gri_host = prop.getProperty("gri_host");
        final int gri_port = Integer.parseInt(prop.getProperty("gri_port"));

        // Create the Shared Region
        final GRIInterface gri = new GRIRemote(gri_host, gri_port);
        final DTTQInterface dttq = new DepartureTerminalTransferQuay(gri);

        // Stopping criteria
        final AtomicInteger done = new AtomicInteger(0);

        try {
            // Setup the server
            final ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);

            // Serve all the clients (Bus Driver, Passenger)
            while (done.get() < 2) {
                try {
                    final Handler handler = new Handler(serverSocket.accept(), dttq, done);
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
        private final DTTQInterface dttq;
        private final AtomicInteger done;

        /**
         * Create a new handler.
         * 
         * @param socket client socket used for communication
         * @param dttq   shared memory implementation {@link DTTQInterface}
         * @param done   atomic variable used for the stopping criteria
         *               {@link AtomicInteger}
         */
        Handler(final Socket socket, final DTTQInterface dttq, final AtomicInteger done) {
            this.socket = socket;
            this.dttq = dttq;
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
                    case "dttq_close":
                        // Instead of closing the log it marks the task as done
                        done.incrementAndGet();
                        reply = new MessageReply(request.type);
                        break;
                    case "dttq_leaveTheBus":
                        dttq.leaveTheBus(Utils.cast(request.argInt0));
                        reply = new MessageReply(request.type);
                        break;
                    case "dttq_parkTheBusAndLetPassOff":
                        dttq.parkTheBusAndLetPassOff(Utils.cast(request.argInt0));
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