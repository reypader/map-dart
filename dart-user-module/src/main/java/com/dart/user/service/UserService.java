package com.dart.user.service;

import com.dart.user.api.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
public interface UserService {

    CheckEmailResponse checkEmailUsage(String email);

    void createRegistration(RegistrationRequest request);

    void verifyUser(String creationCode);

    AuthenticationResponse authenticateBasicUser(AuthenticationRequest request, HttpServletRequest httpRequest);

    AuthenticationResponse authenticateFacebookUser(AuthenticationRequest request, HttpServletRequest httpRequest);

    AuthenticationResponse authenticateGoogleUser(AuthenticationRequest request, HttpServletRequest httpRequest);

}
