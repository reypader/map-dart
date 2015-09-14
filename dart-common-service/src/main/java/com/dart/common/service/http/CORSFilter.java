package com.dart.common.service.http;

import com.dart.common.service.http.exception.NotAllowedException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.NotActiveException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Adapted from Bill Burke's Implementation of CorsFilter in RestEasy.
 * <p/>
 * Handles CORS requests both preflight and simple CORS requests. You must bind this as a singleton and set up
 * allowedOrigins and other settings to use.
 *
 * @author RMPader (modifications)
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CORSFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(CORSFilter.class.getName());

    protected boolean allowCredentials = true;
    protected String allowedMethods = "GET, POST, PUT, DELETE, OPTIONS, HEAD";
    protected String allowedHeaders = "ORIGIN, CONTENT-TYPE, ACCEPT, AUTHORIZATION";
    protected String exposedHeaders;
    protected int corsMaxAge = 600;
    protected Set<String> allowedOrigins = new HashSet<>();

    public CORSFilter() {
        allowedOrigins.add("https://www.travler.com");
    }

    private void preflight(HttpServletRequest requestContext, HttpServletResponse responseContext, String origin) {
        checkOrigin(requestContext, responseContext, origin);
        String requestMethods = requestContext.getHeader(CORSHeaders.ACCESS_CONTROL_REQUEST_METHOD.getValue());
        if (requestMethods != null) {
            if (allowedMethods != null) {
                requestMethods = this.allowedMethods;
            }
            responseContext.setHeader(CORSHeaders.ACCESS_CONTROL_ALLOW_METHODS.getValue(), requestMethods);
        }
        String allowHeaders = requestContext.getHeader(CORSHeaders.ACCESS_CONTROL_REQUEST_HEADERS.getValue());
        if (allowHeaders != null) {
            if (allowedHeaders != null) {
                allowHeaders = this.allowedHeaders;
            }
            responseContext.setHeader(CORSHeaders.ACCESS_CONTROL_ALLOW_HEADERS.getValue(), allowHeaders);
        }
        if (corsMaxAge > -1) {
            responseContext.setHeader(CORSHeaders.ACCESS_CONTROL_MAX_AGE.getValue(), String.valueOf(corsMaxAge));
        }

    }

    private void checkOrigin(HttpServletRequest requestContext, HttpServletResponse responseContext, String origin) {
        if (origin != null && !allowedOrigins.contains("*") && !allowedOrigins.contains(origin)) {
            throw new NotAllowedException("Origin not allowed: " + origin);
        } else {
            responseContext.setStatus(HttpServletResponse.SC_OK);
            responseContext.setHeader(CORSHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.getValue(), origin);
            responseContext.setHeader(CORSHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS.getValue(), Boolean.toString(allowCredentials));
            if (exposedHeaders != null) {
                responseContext.setHeader(CORSHeaders.ACCESS_CONTROL_EXPOSE_HEADERS.getValue(), exposedHeaders);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requestContext = (HttpServletRequest) request;
        HttpServletResponse responseContext = (HttpServletResponse) response;
        try {
            String origin = requestContext.getHeader("ORIGIN");
            if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
                LOGGER.info("Attempting to set pre-flight response headers.");
                preflight(requestContext, responseContext, origin);
            } else {
                checkOrigin(requestContext, responseContext, origin);
                LOGGER.info("Origin is allowed. Executing next filter.");
                chain.doFilter(request, response);
            }
        } catch (NotActiveException e) {
            LOGGER.warning(e.getMessage());
            responseContext.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {

    }
}
