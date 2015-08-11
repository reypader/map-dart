package com.dart.data.exception;

/**
 * @author RMPader
 */
public class EntityCollisionException extends RuntimeException {

    public EntityCollisionException(String message) {
        super(message);
    }

    public EntityCollisionException(String message, Throwable cause) {
        super(message, cause);
    }

}
