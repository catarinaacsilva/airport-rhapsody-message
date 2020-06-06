package pt.ua.deti.common;

import java.io.Serializable;

/**
 * Generic Message used for Message Passing synchronization mechanism. This
 * class represents a method request.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class MessageRequest implements Serializable {
    /**
     * serialization runtime associates with each serializable class a version
     * number
     */
    private static final long serialVersionUID = 1L;
    /** message type, specifies the method */
    public final String type;
    /** boolean argument */
    public final boolean argBool;
    /** int argument */
    public final int argInt0, argInt1, argInt2, argInt3, argInt4;
    /** Object argument */
    public final Object argObj;

    /**
     * Creates a {@link MessageRequest}
     * 
     * @param type message type
     */
    public MessageRequest(final String type) {
        this.type = type;
        this.argBool = false;
        this.argInt0 = 0;
        this.argInt1 = 0;
        this.argInt2 = 0;
        this.argInt3 = 0;
        this.argInt4 = 0;
        this.argObj = null;
    }

    /**
     * Creates a {@link MessageRequest}
     * 
     * @param type    message type
     * @param argInt0 int argument
     */
    public MessageRequest(final String type, final int argInt0) {
        this.type = type;
        this.argBool = false;
        this.argInt0 = argInt0;
        this.argInt1 = 0;
        this.argInt2 = 0;
        this.argInt3 = 0;
        this.argInt4 = 0;
        this.argObj = null;
    }

    /**
     * Creates a {@link MessageRequest}
     * 
     * @param type    message type
     * @param argBool boolean argument
     */
    public MessageRequest(final String type, final boolean argBool) {
        this.type = type;
        this.argBool = argBool;
        this.argInt0 = 0;
        this.argInt1 = 0;
        this.argInt2 = 0;
        this.argInt3 = 0;
        this.argInt4 = 0;
        this.argObj = null;
    }

    /**
     * Creates a {@link MessageRequest}
     * 
     * @param type    message type
     * @param argInt0 int argument
     * @param argInt1 int argument
     */
    public MessageRequest(final String type, final int argInt0, final int argInt1) {
        this.type = type;
        this.argBool = false;
        this.argInt0 = argInt0;
        this.argInt1 = argInt1;
        this.argInt2 = 0;
        this.argInt3 = 0;
        this.argInt4 = 0;
        this.argObj = null;
    }

    /**
     * Creates a {@link MessageRequest}
     * 
     * @param type    message type
     * @param argInt0 int argument
     * @param argInt1 int argument
     * @param argInt2 int argument
     * @param argInt3 int argument
     * @param argInt4 int argument
     */
    public MessageRequest(final String type, final int argInt0, final int argInt1, final int argInt2, final int argInt3,
            final int argInt4) {
        this.type = type;
        this.argBool = false;
        this.argInt0 = argInt0;
        this.argInt1 = argInt1;
        this.argInt2 = argInt2;
        this.argInt3 = argInt3;
        this.argInt4 = argInt4;
        this.argObj = null;
    }

    /**
     * Creates a {@link MessageRequest}
     * 
     * @param type   message type
     * @param argObj Object argument
     */
    public MessageRequest(final String type, final Object argObj) {
        this.type = type;
        this.argBool = false;
        this.argInt0 = 0;
        this.argInt1 = 0;
        this.argInt2 = 0;
        this.argInt3 = 0;
        this.argInt4 = 0;
        this.argObj = argObj;
    }

    /**
     * Creates a {@link MessageRequest}
     * 
     * @param type    message type
     * @param argBool boolean argument
     * @param argObj  Object argument
     */
    public MessageRequest(final String type, final boolean argBool, final Object argObj) {
        this.type = type;
        this.argBool = argBool;
        this.argInt0 = 0;
        this.argInt1 = 0;
        this.argInt2 = 0;
        this.argInt3 = 0;
        this.argInt4 = 0;
        this.argObj = argObj;
    }
}