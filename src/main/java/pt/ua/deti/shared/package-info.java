/**
 * Shared Regions
 * <p>
 * Containing the data types associated with regions of thread communication and
 * synchronization.
 * </p>
 * 
 * In the following code snippet you can find the generic code of a server:
 * 
 * <pre>{@code 
 * public static void main(final String[] args) {
    // Read the configuration file
    final Properties prop = Utils.loadProperties("config.properties");
    // Server port
    final int port = Integer.parseInt(prop.getProperty("sm_port"));
    // Create the Shared Region
    final Interface sharedMemory = new SharedMemoryImplementation();

    // Stopping criteria
    final AtomicInteger done = new AtomicInteger(0);

    try {
        // Setup the server
        final ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);

        // Serve all the clients (Bus driver, Porter, Passenger)
        while (done.get() < 3) {
            try {
                final Handler handler = new Handler(serverSocket.accept(), al, done);
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
}
 * }</pre>
 */
package pt.ua.deti.shared;