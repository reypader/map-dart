package com.dart.user.api.google;

import com.dart.user.api.AuthenticationRequest;
import com.dart.user.api.AuthenticationResponse;
import com.dart.user.api.CheckEmailResponse;
import com.dart.user.api.RegistrationRequest;
import com.dart.user.service.UserService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.inject.Inject;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RMPader
 */
@Api(canonicalName = "User Management",
        description = "API for doing user-related actions such as CRUD, and authentication",
        name = "user", title = "User Management API")
public class UserEndpoint {

    private UserService service;

    @Inject
    public UserEndpoint(UserService service) {
        this.service = service;
    }

    @ApiMethod(name = "email.check", path = "email/check", httpMethod = HttpMethod.GET)
    public CheckEmailResponse checkEmail(@Named("email") String email) {
        return service.checkEmailUsage(email);
    }

    @ApiMethod(name = "register", path = "register", httpMethod = HttpMethod.POST)
    public void register(RegistrationRequest request) {
        service.createRegistration(request);
    }

    @ApiMethod(name = "verify", path = "verify", httpMethod = HttpMethod.GET)
    public void verify(@Named("creation_code") String creationCode) {
        service.verifyUser(creationCode);
    }

    @ApiMethod(name = "auth.basic", path = "auth/basic", httpMethod = HttpMethod.POST)
    public AuthenticationResponse basicAuth(HttpServletRequest httpRequest, AuthenticationRequest request) {
        return service.authenticateBasicUser(request, httpRequest);
    }

    @ApiMethod(name = "auth.facebook", path = "auth/facebook", httpMethod = HttpMethod.POST)
    public AuthenticationResponse fbAuth(HttpServletRequest httpRequest, AuthenticationRequest request, @Named("additional_data") String data) throws UnsupportedEncodingException {
        Map<String, String> additionalData = parseAdditionalData(data);
        request.setData(additionalData);
        return service.authenticateFacebookUser(request, httpRequest);
    }

    @ApiMethod(name = "auth.google", path = "auth/google", httpMethod = HttpMethod.POST)
    public AuthenticationResponse gpAuth(HttpServletRequest httpRequest, AuthenticationRequest request, @Named("additional_data") String data) throws UnsupportedEncodingException {
        Map<String, String> additionalData = parseAdditionalData(data);
        request.setData(additionalData);
        return service.authenticateGoogleUser(request, httpRequest);
    }

    private Map<String, String> parseAdditionalData(@Named("data") String data) throws UnsupportedEncodingException {
        String[] pairs = data.split(";");
        Map<String, String> additionalData = new HashMap<>();
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            additionalData.put(parts[0], URLDecoder.decode(parts[1], "UTF-8"));
        }
        return additionalData;
    }
}
