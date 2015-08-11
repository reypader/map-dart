package com.dart.data.exception;

/**
 * Wrapper exception for exceptions that indicate that an entity being acted upon does not exist.
 *
 * @author RMPader
 */
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
