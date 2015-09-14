package com.dart.common.service.http.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author RMPader
 */
public class NotAllowedException extends WebApplicationException {
    public NotAllowedException(String message) {
        super(Response.status(Response.Status.FORBIDDEN)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}
