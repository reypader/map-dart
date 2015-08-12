package com.dart.user.service;

import com.dart.common.service.auth.SessionService;
import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.mail.MailSenderService;
import com.dart.common.service.properties.PropertiesProvider;
import com.dart.common.service.util.TemplateHelper;
import com.dart.data.domain.Identity;
import com.dart.data.domain.Registration;
import com.dart.data.domain.User;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.factory.RegistrationFactory;
import com.dart.data.factory.UserFactory;
import com.dart.data.repository.IdentityRepository;
import com.dart.data.repository.RegistrationRepository;
import com.dart.data.repository.UserRepository;
import com.dart.user.api.AuthenticationRequest;
import com.dart.user.api.AuthenticationResponse;
import com.dart.user.api.CheckEmailResponse;
import com.dart.user.api.RegistrationRequest;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RMPader
 */
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final RegistrationRepository registrationRepository;
    private final RegistrationFactory registrationFactory;
    private final SessionService sessionService;
    private final IdentityRepository identityRepository;
    private final IdentityFactory identityFactory;
    private final PropertiesProvider propertiesProvider;
    private final TokenVerificationService facebookTokenVerificationService;
    private MailSenderService mailSender;
    private String emailTemplate;


    public UserServiceImpl(TokenVerificationService facebookTokenVerificationService, SessionService SessionService, UserRepository userRepository, UserFactory userFactory, IdentityRepository identityRepository, IdentityFactory identityFactory, RegistrationRepository registrationRepository, RegistrationFactory registrationFactory, PropertiesProvider propertiesProvider) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.registrationRepository = registrationRepository;
        this.registrationFactory = registrationFactory;
        this.identityRepository = identityRepository;
        this.identityFactory = identityFactory;
        this.sessionService = SessionService;
        this.propertiesProvider = propertiesProvider;
        this.facebookTokenVerificationService = facebookTokenVerificationService;
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
    public void verifyUser(String creationCode) {
        Registration registration = registrationRepository.retrieve(creationCode);
        User user = userFactory.createUser(registration.getEmail(), registration.getDisplayName());
        user = userRepository.add(user);
        Identity identity = identityFactory.createIdentity(user, "self", registration.getEmail());
        identity.addData("password", registration.getPassword());
        identityRepository.add(identity);
        registrationRepository.delete(registration);
    }

    @Override
    public AuthenticationResponse authenticateBasicUser(AuthenticationRequest request, HttpServletRequest httpRequest) {
        Identity identity = identityRepository.findIdentityFromProvider(request.getEmail(), "self");
        AuthenticationResponse response = new AuthenticationResponse();
        response.setIdentityProvider("self");
        if (BCrypt.checkpw(request.getToken(), identity.getData().get("password").toString())) {
            String token = sessionService.generateSession(identity.getUser(), httpRequest);
            response.setToken(token);
        }
        return response;
    }

    @Override
    public AuthenticationResponse authenticateFacebookUser(AuthenticationRequest request, HttpServletRequest httpRequest) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setIdentityProvider("facebook");
        //TODO verify token
        if (facebookTokenVerificationService.verifyToken(request.getToken(), request.getData().get("id"))) {
            Identity identity = identityRepository.findIdentityFromProvider(request.getData().get("id"), "facebook");
            if (identity == null) {
                User user = userRepository.retrieve(request.getEmail());
                if (user == null) {
                    user = userFactory.createUser(request.getEmail(), request.getData().get("name"));
                    user = userRepository.add(user);
                }
                identity = identityFactory.createIdentity(user, "facebook", request.getData().get("id"));
                identityRepository.add(identity);
            }
            String token = sessionService.generateSession(identity.getUser(), httpRequest);
            response.setToken(token);
        }
        return response;
    }

    @Override
    public AuthenticationResponse authenticateGoogleUser(AuthenticationRequest request, HttpServletRequest httpRequest) {
        return null;
    }


    public void setMailSender(MailSenderService mailSender, String signinEmailTemplatePath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(signinEmailTemplatePath));
        this.emailTemplate = new String(encoded, "UTF-8");
        this.mailSender = mailSender;
    }

}
