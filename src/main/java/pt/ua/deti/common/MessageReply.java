package pt.ua.deti.common;

import java.io.Serializable;

/**
 * Generic Message used for Message Passing synchronization mechanism.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MessageReply implements Serializable {

    private static final long serialVersionUID = 1L;
    public final String type;
    public final int retCode, retInt;
    public final Object retObj;
    public final boolean retBool;

    public MessageReply(final String type) {
        this.type = type;
        this.retCode = 0;
        this.retInt = 0;
        this.retObj = null;
        this.retBool = false;
    }

    public MessageReply(final String type, final int retCode) {
        this.type = type;
        this.retCode = retCode;
        this.retInt = 0;
        this.retObj = null;
        this.retBool = false;
    }

    public MessageReply(final String type, final int retCode, final int retInt) {
        this.type = type;
        this.retCode = retCode;
        this.retInt = retInt;
        this.retObj = null;
        this.retBool = false;
    }

    public MessageReply(final String type, final boolean retBool) {
        this.type = type;
        this.retCode = 0;
        this.retInt = 0;
        this.retObj = null;
        this.retBool = retBool;
    }

    public MessageReply(final String type, final int retCode, final Object retObj) {
        this.type = type;
        this.retCode = retCode;
        this.retInt = 0;
        this.retObj = retObj;
        this.retBool = false;
    }
}