package com.dart.user.service;

import com.dart.common.service.auth.HttpRequestAuthorizationService;
import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.auth.facebook.Facebook;
import com.dart.common.service.auth.google.Google;
import com.dart.common.service.auth.google.Recaptcha;
import com.dart.common.service.exception.IllegalTransactionException;
import com.dart.common.service.mail.MailSenderService;
import com.dart.common.service.properties.PropertiesProvider;
import com.dart.common.service.util.IPAddressHelper;
import com.dart.common.service.util.TemplateHelper;
import com.dart.data.domain.Identity;
import com.dart.data.domain.Registration;
import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
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
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author RMPader
 */
@Singleton
public class UserServiceImpl implements UserService {
    private static Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final RegistrationRepository registrationRepository;
    private final RegistrationFactory registrationFactory;
    private final HttpRequestAuthorizationService httpRequestAuthorizationService;
    private final IdentityRepository identityRepository;
    private final IdentityFactory identityFactory;
    private final PropertiesProvider propertiesProvider;
    private final TokenVerificationService facebookTokenVerificationService;
    private final TokenVerificationService googleTokenVerificationService;
    private final TokenVerificationService recaptchaTokenVerificationService;
    private final MailSenderService mailSender;
    private final String emailTemplate;

    @Inject
    public UserServiceImpl(@Recaptcha TokenVerificationService recaptchaTokenVerificationService, @Facebook TokenVerificationService facebookTokenVerificationService, @Google TokenVerificationService googleTokenVerificationService, HttpRequestAuthorizationService HttpRequestAuthorizationService, UserRepository userRepository, UserFactory userFactory, IdentityRepository identityRepository, IdentityFactory identityFactory, RegistrationRepository registrationRepository, RegistrationFactory registrationFactory, PropertiesProvider propertiesProvider, MailSenderService mailSender) throws IOException {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.registrationRepository = registrationRepository;
        this.registrationFactory = registrationFactory;
        this.identityRepository = identityRepository;
        this.identityFactory = identityFactory;
        this.httpRequestAuthorizationService = HttpRequestAuthorizationService;
        this.propertiesProvider = propertiesProvider;
        this.facebookTokenVerificationService = facebookTokenVerificationService;
        this.googleTokenVerificationService = googleTokenVerificationService;
        this.recaptchaTokenVerificationService = recaptchaTokenVerificationService;
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
        LOGGER.info("Checking usage of email: " + email);
        User user = userRepository.retrieveByEmail(email);
        CheckEmailResponse response = new CheckEmailResponse();
        response.setEmailUsed(user != null);
        return response;
    }

    @Override
    public void createRegistration(RegistrationRequest request) {
        try {
            LOGGER.info("Creating registration for: " + request.getEmail());
            String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            Registration registration = registrationFactory.createRegistration(request.getEmail(), request.getDisplayName(), hashed);
            Registration newRegistration = registrationRepository.add(registration);
            Map<String, String> data = new HashMap<>();
            data.put("name", request.getDisplayName());
            data.put("link", propertiesProvider.getUserWebsiteURL() + "/signin.html?registration=" + newRegistration.getId());
            String body = TemplateHelper.render(emailTemplate, data);
            mailSender.sendMail(registration.getEmail(), registration.getDisplayName(), "Registration Confirmation", body);
        } catch (MessagingException e) {
            LOGGER.log(Level.WARNING, "Failed to send email to " + request.getEmail());
            registrationRepository.deleteRegistrationForEmail(request.getEmail());
        }
    }

    @Override
    public VerificationResponse verifyUser(String creationCode) {
        LOGGER.info("Attempting to verify registration:" + creationCode);
        VerificationResponse response = new VerificationResponse();
        Registration registration = registrationRepository.retrieve(creationCode);
        try {
            if (registration != null && !checkEmailUsage(registration.getEmail()).isEmailUsed()) {
                User user = userFactory.createUser(registration.getEmail(), registration.getDisplayName());
                user = userRepository.add(user);
                Identity identity = identityFactory.createIdentity(user, "basic", registration.getEmail());
                identity.addData("password", registration.getPassword());
                identityRepository.add(identity);
                response.setError(false);
            } else {
                LOGGER.log(Level.WARNING, "Tried to verify a non-existent registration: " + creationCode);
                response.setError(true);
            }
            return response;
        } catch (EntityCollisionException e) {
            registrationRepository.delete(registration);
            LOGGER.log(Level.WARNING, "Tried to verify a registration but the user was already known to us: " + registration.getEmail());
            response.setError(true);
            return response;
        } finally {
            if (registration != null) {
                registrationRepository.deleteRegistrationForEmail(registration.getEmail());
            }
        }
    }

    @Override
    public AuthenticationResponse authenticateBasicUser(AuthenticationRequest request, HttpServletRequest httpRequest) {
        LOGGER.info("Doing a basic authentication for user: " + request.getEmail());
        Identity identity = identityRepository.findIdentityFromProvider(request.getEmail(), "basic");
        AuthenticationResponse response = new AuthenticationResponse();
        response.setIdentityProvider("basic");
        if (identity != null && BCrypt.checkpw(request.getToken(), identity.getData().get("password").toString())) {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_YEAR, propertiesProvider.getDefaultTokenValidityDays());
            Date later = now.getTime();
            User user = identity.getUser();
            String token = httpRequestAuthorizationService.generateToken(later, user, httpRequest);
            response.setToken(token);
            response.setUser(user.getId());
            response.setDisplayName(user.getDisplayName());
            LOGGER.info("Authentication successful for user: " + request.getEmail());
        } else {
            LOGGER.info("Authentication failed for user: " + request.getEmail());
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
        LOGGER.info("Doing a " + providerName + " authentication for user: " + request.getEmail());
        AuthenticationResponse response = new AuthenticationResponse();
        response.setIdentityProvider(providerName);
        if (tokenVerificationService.verifyToken(request.getToken(), request.getData().get("id"))) {
            Identity identity = identityRepository.findIdentityFromProvider(request.getData().get("id"), providerName);
            if (identity == null) {
                User user = userRepository.retrieveByEmail(request.getEmail());
                if (user == null) {
                    user = userFactory.createUser(request.getEmail(), request.getData().get("name"));
                    user = userRepository.add(user);
                }
                identity = identityFactory.createIdentity(user, providerName, request.getData().get("id"));
                identityRepository.add(identity);
            }
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_YEAR, propertiesProvider.getDefaultTokenValidityDays());
            Date later = now.getTime();
            User user = identity.getUser();
            String token = httpRequestAuthorizationService.generateToken(later, user, httpRequest);
            response.setToken(token);
            response.setUser(user.getId());
            response.setDisplayName(user.getDisplayName());
            LOGGER.info("Authentication successful for user: " + request.getEmail());
        } else {
            LOGGER.info("Authentication failed for user: " + request.getEmail());

        }
        return response;
    }

    @Override
    public RecaptchaResponse validateRecaptchaResult(RecaptchaRequest request, HttpServletRequest httpRequest) {
        LOGGER.info("Attempting to validate recaptcha code: " + request.getRecaptchaResult());
        boolean result = recaptchaTokenVerificationService.verifyToken(request.getRecaptchaResult(), IPAddressHelper.getIPAddress(httpRequest));
        RecaptchaResponse response = new RecaptchaResponse();
        response.setUserIsHuman(result);
        return response;
    }

    @Override
    public void updateUser(UpdateUserRequest request, User user) throws IllegalTransactionException {
        try {
            if (user.getId().equals(request.getId())) {
                user.setPhotoURL(request.getPhotoURL());
                user.setDescription(request.getDescription());
                user.setDisplayName(request.getDisplayName());
                userRepository.update(user);
            } else {
                throw new IllegalTransactionException("User being updated is not the same as the identified user");
            }
        } catch (EntityNotFoundException e) {
            throw new IllegalTransactionException("User being updated does not exist");
        }

    }
}
