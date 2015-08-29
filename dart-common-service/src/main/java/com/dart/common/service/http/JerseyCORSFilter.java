package com.dart.common.service.http;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Adapted from Bill Burke's Implementation of CorsFilter in RestEasy.
 *
 * Handles CORS requests both preflight and simple CORS requests.
 * You must bind this as a singleton and set up allowedOrigins and other settings to use.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class JerseyCORSFilter implements ContainerRequestFilter, ContainerResponseFilter {
    protected boolean allowCredentials = true;
    protected String allowedMethods="GET, POST, PUT, DELETE, OPTIONS, HEAD";
    protected String allowedHeaders="ORIGIN, CONTENT-TYPE, ACCEPT, AUTHORIZATION";
    protected String exposedHeaders;
    protected int corsMaxAge = 600;
    protected Set<String> allowedOrigins = new HashSet<>();

    public JerseyCORSFilter(){
        allowedOrigins.add("https://www.travler.com");
    }

    @Override
    public ContainerRequest filter(ContainerRequest requestContext) {
        String origin = requestContext.getHeaderValue("ORIGIN");
        if (origin == null) {
            return requestContext;
        }
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            preflight(origin, requestContext);
        } else {
            checkOrigin(requestContext, origin);
        }
        return requestContext;
    }

    @Override
    public ContainerResponse filter(ContainerRequest requestContext, ContainerResponse responseContext) {
        String origin = requestContext.getHeaderValue("ORIGIN");
        if (origin == null || requestContext.getMethod().equalsIgnoreCase("OPTIONS") || requestContext.getProperties().get("cors.failure") != null) {
            // don't do anything if origin is null, its an OPTIONS request, or cors.failure is set
            return responseContext;
        }
        responseContext.getHttpHeaders().add(CORSHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.getValue(), origin);
        if (allowCredentials)
            responseContext.getHttpHeaders().add(CORSHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS.getValue(), "true");

        if (exposedHeaders != null) {
            responseContext.getHttpHeaders().add(CORSHeaders.ACCESS_CONTROL_EXPOSE_HEADERS.getValue(), exposedHeaders);
        }
        return responseContext;
    }


    protected void preflight(String origin, ContainerRequest requestContext) {
        checkOrigin(requestContext, origin);

        Response.ResponseBuilder builder = Response.ok();
        builder.header(CORSHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.getValue(), origin);
        if (allowCredentials) builder.header(CORSHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS.getValue(), "true");
        String requestMethods = requestContext.getHeaderValue(CORSHeaders.ACCESS_CONTROL_REQUEST_METHOD.getValue());
        if (requestMethods != null) {
            if (allowedMethods != null) {
                requestMethods = this.allowedMethods;
            }
            builder.header(CORSHeaders.ACCESS_CONTROL_ALLOW_METHODS.getValue(), requestMethods);
        }
        String allowHeaders = requestContext.getHeaderValue(CORSHeaders.ACCESS_CONTROL_REQUEST_HEADERS.getValue());
        if (allowHeaders != null) {
            if (allowedHeaders != null) {
                allowHeaders = this.allowedHeaders;
            }
            builder.header(CORSHeaders.ACCESS_CONTROL_ALLOW_HEADERS.getValue(), allowHeaders);
        }
        if (corsMaxAge > -1) {
            builder.header(CORSHeaders.ACCESS_CONTROL_MAX_AGE.getValue(), corsMaxAge);
        }
        throw new WebApplicationException(builder.build());

    }

    protected void checkOrigin(ContainerRequest requestContext, String origin) {
        if (!allowedOrigins.contains("*") && !allowedOrigins.contains(origin)) {
            requestContext.getProperties().put("cors.failure", true);
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

}
