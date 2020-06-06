package pt.ua.deti.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

/**
 * Utils library.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class Utils {
    /**
     * Utility class, lets make the constructor private.
     */
    private Utils() {
    }

    /**
     * Casts one object type into another suppressing compilation warning.
     * 
     * @param o object to be casted
     * @param <T> the new object type
     * @return the casted object
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    /**
     * Sends a {@link MessageRequest} to a server and waits for the
     * {@link MessageReply}.
     * 
     * @param hostname server's hostname
     * @param port     server's port
     * @param request  {@link MessageRequest} request
     * @return {@link MessageReply} reply
     */
    public static MessageReply remoteMethod(final String hostname, final int port, final MessageRequest request) {
        MessageReply reply = null;
        try {
            Socket socket = new Socket(hostname, port);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

            os.writeObject(request);
            reply = Utils.cast(is.readObject());

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reply;
    }

    /**
     * Load the properties file with the kafka configuration
     * 
     * @param file the path to the configuration
     * @return {@link Properties} class
     */
    public static Properties loadProperties(final String file) {
        try {
            final Properties properties = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            final InputStream inputStream = loader.getResourceAsStream(file);
            properties.load(inputStream);
            return properties;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}