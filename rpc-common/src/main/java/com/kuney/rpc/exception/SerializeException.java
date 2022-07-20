package com.kuney.rpc.exception;

/**
 * @author kuneychen
 * @since 2022/7/20 16:33
 */
public class SerializeException extends RuntimeException {

    public SerializeException() {
        super();
    }

    public SerializeException(String message) {
        super(message);
    }
}
