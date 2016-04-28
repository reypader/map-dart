package com.dart.common.service.http.exception;

/**
 * Created by rpader on 4/28/16.
 */
public class WebClientException extends Exception {

    private final int statusCode;

    public WebClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
