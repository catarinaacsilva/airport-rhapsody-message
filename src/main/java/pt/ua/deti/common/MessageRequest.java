package pt.ua.deti.common;

import java.io.Serializable;

/**
 * Generic Message used for Message Passing synchronization mechanism.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    public final String type;
    public final boolean argBool;
    public final int argInt0, argInt1, argInt2, argInt3, argInt4;
    public final Object argObj;

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