package com.dart.user.service;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.auth.google.Recaptcha;
import com.dart.common.service.exception.IllegalTransactionException;
import com.dart.common.service.http.exception.InternalServerException;
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
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author RMPader
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private TokenVerificationService recaptchaTokenVerificationService;

    private UserRepository userRepository;

    private UserFactory userFactory;

    private RegistrationRepository registrationRepository;

    private RegistrationFactory registrationFactory;

    private IdentityRepository identityRepository;

    private IdentityFactory identityFactory;

    private PropertiesProvider propertiesProvider;

    private MailSenderService mailSender;

    private String emailTemplate;

    @Autowired
    public UserServiceImpl(@Recaptcha TokenVerificationService recaptchaTokenVerificationService,
                           UserRepository userRepository, UserFactory userFactory,
                           RegistrationRepository registrationRepository,
                           RegistrationFactory registrationFactory,
                           IdentityRepository identityRepository, IdentityFactory identityFactory,
                           PropertiesProvider propertiesProvider, MailSenderService mailSender) {
        this.recaptchaTokenVerificationService = recaptchaTokenVerificationService;
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.registrationRepository = registrationRepository;
        this.registrationFactory = registrationFactory;
        this.identityRepository = identityRepository;
        this.identityFactory = identityFactory;
        this.propertiesProvider = propertiesProvider;
        this.mailSender = mailSender;

        InputStream stream = getClass().getClassLoader()
                                       .getResourceAsStream("emailVerification.html");
        StringBuffer buf = new StringBuffer();
        Scanner sc = new Scanner(stream);
        while (sc.hasNext()) {
            buf.append(sc.nextLine());
        }
        this.emailTemplate = buf.toString();
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
            Registration registration = registrationFactory.createRegistration(request.getEmail(),
                                                                               request.getDisplayName(), hashed);
            Registration newRegistration = registrationRepository.add(registration);
            Map<String, String> data = new HashMap<>();
            data.put("name", request.getDisplayName());
            data.put("link",
                     propertiesProvider.getUserWebsiteURL() + "/signin.html?registration=" + newRegistration.getId());
            String body = TemplateHelper.render(emailTemplate, data);
            mailSender.sendMail(registration.getEmail(), registration.getDisplayName(), "Registration Confirmation",
                                body);
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
            LOGGER.log(Level.WARNING,
                       "Tried to verify a registration but the user was already known to us: " + registration.getEmail());
            response.setError(true);
            return response;
        } finally {
            if (registration != null) {
                registrationRepository.deleteRegistrationForEmail(registration.getEmail());
            }
        }
    }

    @Override
    public RecaptchaResponse validateRecaptchaResult(RecaptchaRequest request, HttpServletRequest httpRequest) {
        try {
            LOGGER.info("Attempting to validate recaptcha code: " + request.getRecaptchaResult());
            boolean result = recaptchaTokenVerificationService.verifyToken(request.getRecaptchaResult(),
                                                                           IPAddressHelper.getIPAddress(httpRequest));
            RecaptchaResponse response = new RecaptchaResponse();
            response.setUserIsHuman(result);
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error encountered while validating Recaptcha", e);
            throw new InternalServerException();
        }
    }

    @Override
    public void updateUser(UpdateUserRequest request, User user) throws IllegalTransactionException {
        try {
            if (user.getId()
                    .equals(request.getId())) {
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
