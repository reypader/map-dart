package com.dart.user.api.jersey;

import com.dart.user.api.*;
import com.dart.user.service.UserService;
import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RMPader
 */
@Path("/user")
@Api(value = "/user", description = "API for doing user-related actions such as CRUD, and authentication")
public class UserEndpoint {

    private UserService service;

    @Inject
    public UserEndpoint(UserService service) {
        this.service = service;
    }

    @GET
    @Path("/email/check")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check whether the given email is already registered and verified.",
            response = CheckEmailResponse.class)
    public CheckEmailResponse checkEmail(@QueryParam("email") String email) {
        return service.checkEmailUsage(email);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a registration entry. ",
            notes = "After doing so, an email will be sent to the email provided with a verification link. This does not yet mean that the user is registered.")
    public void register(RegistrationRequest request) {
        service.createRegistration(request);
    }

    @GET
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Verify the corresponding registration entry and register the user",
            notes = "At this point, the User will be given a record and is now truly registered",
            response = VerificationResponse.class)
    public VerificationResponse verify(@QueryParam("creation_code") String creationCode) {
        return service.verifyUser(creationCode);
    }

    @POST
    @Path("/auth/basic")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Authenticate using the email and password",
            notes = "The password must be Hashed and Base64 encoded",
            response = AuthenticationResponse.class)
    public AuthenticationResponse basicAuth(@Context HttpServletRequest httpRequest, AuthenticationRequest request) {
        return service.authenticateBasicUser(request, httpRequest);
    }

    @POST
    @Path("/auth/facebook")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Authenticate using the email and access token provided by Facebook",
            notes = "The access token will still be verified in the backend before processing. If the email is not yet associated with a user, a new User will be created. Otherwise, this Facebook Identity will be associated.",
            response = AuthenticationResponse.class)
    public AuthenticationResponse fbAuth(@Context HttpServletRequest httpRequest, AuthenticationRequest request) throws UnsupportedEncodingException {
        return service.authenticateFacebookUser(request, httpRequest);
    }

    @POST
    @Path("/auth/google")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Authenticate using the email and access token provided by Google",
            notes = "The access token will still be verified in the backend before processing. If the email is not yet associated with a user, a new User will be created. Otherwise, this Google Identity will be associated.",
            response = AuthenticationResponse.class)
    public AuthenticationResponse gpAuth(@Context HttpServletRequest httpRequest, AuthenticationRequest request) throws UnsupportedEncodingException {
        return service.authenticateGoogleUser(request, httpRequest);
    }

    @POST
    @Path("/recaptcha")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Validate the Recaptcha Token with Google",
            response = RecaptchaResponse.class)
    public RecaptchaResponse recaptcha(@Context HttpServletRequest httpRequest, RecaptchaRequest request) throws UnsupportedEncodingException {
        return service.validateRecaptchaResult(request, httpRequest);
    }
}
