package pt.ua.deti.common;

import java.io.Serializable;

/**
 * Generic Message used for Message Passing synchronization mechanism. This
 * class represents a method reply.
 * 
 * @author Catarina Silva
 * @version 1.0
 */
public class MessageReply implements Serializable {
    /**
     * serialization runtime associates with each serializable class a version
     * number
     */
    private static final long serialVersionUID = 1L;
    /** message type, specifies the method */
    public final String type;
    /** return code (0 = success) */
    public final int retCode;
    /** return int value */
    public final int retInt;
    /** return Object value */
    public final Object retObj;
    /** return boolean value */
    public final boolean retBool;

    /**
     * Creates a {@link MessageReply}
     * 
     * @param type message type
     */
    public MessageReply(final String type) {
        this.type = type;
        this.retCode = 0;
        this.retInt = 0;
        this.retObj = null;
        this.retBool = false;
    }

    /**
     * Creates a {@link MessageReply}
     * 
     * @param type    message type
     * @param retCode return code (0 = success)
     */
    public MessageReply(final String type, final int retCode) {
        this.type = type;
        this.retCode = retCode;
        this.retInt = 0;
        this.retObj = null;
        this.retBool = false;
    }

    /**
     * Creates a {@link MessageReply}
     * 
     * @param type    message type
     * @param retCode return code (0 = success)
     * @param retInt  return int value
     */
    public MessageReply(final String type, final int retCode, final int retInt) {
        this.type = type;
        this.retCode = retCode;
        this.retInt = retInt;
        this.retObj = null;
        this.retBool = false;
    }

    /**
     * Creates a {@link MessageReply}
     * 
     * @param type    message type
     * @param retBool return boolean value
     */
    public MessageReply(final String type, final boolean retBool) {
        this.type = type;
        this.retCode = 0;
        this.retInt = 0;
        this.retObj = null;
        this.retBool = retBool;
    }

    /**
     * Creates a {@link MessageReply}
     * 
     * @param type    message type
     * @param retCode return code (0 = success)
     * @param retObj  return Object value
     */
    public MessageReply(final String type, final int retCode, final Object retObj) {
        this.type = type;
        this.retCode = retCode;
        this.retInt = 0;
        this.retObj = retObj;
        this.retBool = false;
    }
}