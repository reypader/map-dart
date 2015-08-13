package com.dart.user.service;

import com.dart.common.service.auth.Facebook;
import com.dart.common.service.auth.Google;
import com.dart.common.service.auth.SessionService;
import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.mail.MailSenderService;
import com.dart.common.service.properties.PropertiesProvider;
import com.dart.common.service.util.TemplateHelper;
import com.dart.data.domain.Identity;
import com.dart.data.domain.Registration;
import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.factory.RegistrationFactory;
import com.dart.data.factory.UserFactory;
import com.dart.data.repository.IdentityRepository;
import com.dart.data.repository.RegistrationRepository;
import com.dart.data.repository.UserRepository;
import com.dart.user.api.*;
import com.google.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author RMPader
 */
@Singleton
public class UserServiceImpl implements UserService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final RegistrationRepository registrationRepository;
    private final RegistrationFactory registrationFactory;
    private final SessionService sessionService;
    private final IdentityRepository identityRepository;
    private final IdentityFactory identityFactory;
    private final PropertiesProvider propertiesProvider;
    private final TokenVerificationService facebookTokenVerificationService;
    private final TokenVerificationService googleTokenVerificationService;
    private final MailSenderService mailSender;
    private final String emailTemplate;

    @Inject
    public UserServiceImpl(@Facebook TokenVerificationService facebookTokenVerificationService, @Google TokenVerificationService googleTokenVerificationService, SessionService SessionService, UserRepository userRepository, UserFactory userFactory, IdentityRepository identityRepository, IdentityFactory identityFactory, RegistrationRepository registrationRepository, RegistrationFactory registrationFactory, PropertiesProvider propertiesProvider, MailSenderService mailSender) throws IOException {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.registrationRepository = registrationRepository;
        this.registrationFactory = registrationFactory;
        this.identityRepository = identityRepository;
        this.identityFactory = identityFactory;
        this.sessionService = SessionService;
        this.propertiesProvider = propertiesProvider;
        this.facebookTokenVerificationService = facebookTokenVerificationService;
        this.googleTokenVerificationService = googleTokenVerificationService;
        InputStream stream = getClass().getClassLoader().getResourceAsStream("emailVerification.html");
        StringBuffer buf = new StringBuffer();
        Scanner sc = new Scanner(stream);
        while (sc.hasNext()) {
            buf.append(sc.nextLine());
        }
        this.emailTemplate = buf.toString();
        this.mailSender = mailSender;
    }

    @Override
    public CheckEmailResponse checkEmailUsage(String email) {
        User user = userRepository.retrieve(email);

        CheckEmailResponse response = new CheckEmailResponse();
        response.setEmailUsed(user != null);
        return response;
    }

    @Override
    public void createRegistration(RegistrationRequest request) {
        String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        Registration registration = registrationFactory.createRegistration(request.getEmail(), request.getDisplayName(), hashed);
        Registration newRegistration = registrationRepository.add(registration);
        if (mailSender != null) {
            Map<String, String> data = new HashMap<>();
            data.put("name", request.getDisplayName());
            data.put("link", propertiesProvider.getUserWebsiteURL() + "/signin.html?registration=" + newRegistration.getId());
            String body = TemplateHelper.render(emailTemplate, data);
            mailSender.sendMail(registration.getEmail(), registration.getDisplayName(), "Registration Confirmation", body);
        }

    }

    @Override
    public VerificationResponse verifyUser(String creationCode) {
        VerificationResponse response = new VerificationResponse();
        Registration registration = registrationRepository.retrieve(creationCode);
        try {
            if (registration != null) {
                User user = userFactory.createUser(registration.getEmail(), registration.getDisplayName());
                user = userRepository.add(user);
                Identity identity = identityFactory.createIdentity(user, "self", registration.getEmail());
                identity.addData("password", registration.getPassword());
                identityRepository.add(identity);
            } else {
                logger.log(Level.WARNING, "Tried to verify a non-existent registration: " + creationCode);
                response.setError(true);
            }
            return response;
        } catch (EntityCollisionException e) {
            registrationRepository.delete(registration);
            logger.log(Level.WARNING, "Tried to verify a registration but the user was already known to us: " + registration.getEmail());
            response.setError(true);
            return response;
        } finally{
            //TODO delete all registration for the same email
            registrationRepository.delete(registration);
        }
    }

    @Override
    public AuthenticationResponse authenticateBasicUser(AuthenticationRequest request, HttpServletRequest httpRequest) {
        Identity identity = identityRepository.findIdentityFromProvider(request.getEmail(), "self");
        AuthenticationResponse response = new AuthenticationResponse();
        response.setIdentityProvider("self");
        if (identity != null && BCrypt.checkpw(request.getToken(), identity.getData().get("password").toString())) {
            String token = sessionService.generateSession(identity.getUser(), httpRequest);
            response.setToken(token);
        }
        return response;
    }

    @Override
    public AuthenticationResponse authenticateFacebookUser(AuthenticationRequest request, HttpServletRequest httpRequest) {
        return getAuthenticationResponse(request, httpRequest, "facebook", facebookTokenVerificationService);
    }

    @Override
    public AuthenticationResponse authenticateGoogleUser(AuthenticationRequest request, HttpServletRequest httpRequest) {
        return getAuthenticationResponse(request, httpRequest, "google", googleTokenVerificationService);
    }

    private AuthenticationResponse getAuthenticationResponse(AuthenticationRequest request, HttpServletRequest httpRequest, String providerName, TokenVerificationService tokenVerificationService) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setIdentityProvider(providerName);
        if (tokenVerificationService.verifyToken(request.getToken(), request.getData().get("id"))) {
            Identity identity = identityRepository.findIdentityFromProvider(request.getData().get("id"), providerName);
            if (identity == null) {
                User user = userRepository.retrieve(request.getEmail());
                if (user == null) {
                    user = userFactory.createUser(request.getEmail(), request.getData().get("name"));
                    user = userRepository.add(user);
                }
                identity = identityFactory.createIdentity(user, providerName, request.getData().get("id"));
                identityRepository.add(identity);
            }
            String token = sessionService.generateSession(identity.getUser(), httpRequest);
            response.setToken(token);
        }
        return response;
    }
}
