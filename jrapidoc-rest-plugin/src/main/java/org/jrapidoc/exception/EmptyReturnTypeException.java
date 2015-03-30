package org.jrapidoc.exception;

/**
 * Created by papa on 21.1.15.
 */
public class EmptyReturnTypeException extends Exception {
    public EmptyReturnTypeException() {
    }

    public EmptyReturnTypeException(String message) {
        super(message);
    }

    public EmptyReturnTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
