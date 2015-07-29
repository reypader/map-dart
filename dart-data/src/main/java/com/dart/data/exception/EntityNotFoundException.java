package com.dart.data.exception;

/**
 * Wrapper exception for exceptions that indicate that an entity being acted upon does not exist.
 *
 * @author RMPader
 */
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
