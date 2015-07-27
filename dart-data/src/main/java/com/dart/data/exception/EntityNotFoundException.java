package com.dart.data.exception;

/**
 * Created by RMPader on 7/27/15.
 */
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
