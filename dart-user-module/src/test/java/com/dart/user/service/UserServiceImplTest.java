package com.dart.user.service;

import com.dart.common.service.auth.SessionService;
import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.mail.MailSenderService;
import com.dart.common.service.properties.FilePropertiesProvider;
import com.dart.common.test.factory.DummyIdentityFactory;
import com.dart.common.test.factory.DummyRegistrationFactory;
import com.dart.common.test.factory.DummyUserFactory;
import com.dart.common.test.repository.DummyIdentityRepository;
import com.dart.common.test.repository.DummyRegistrationRepository;
import com.dart.common.test.repository.DummyUserRepository;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author RMPader
 */
public class UserServiceImplTest {

    private DummyUserRepository dummyUserRepo = new DummyUserRepository();
    private DummyUserFactory dummyUserFactory = new DummyUserFactory();
    private DummyRegistrationRepository dummyRegistrationRepo = new DummyRegistrationRepository();
    private DummyRegistrationFactory dummyRegistrationFactory = new DummyRegistrationFactory();
    private DummyIdentityRepository dummyIdentityRepo = new DummyIdentityRepository();
    private DummyIdentityFactory dummyIdentityFactory = new DummyIdentityFactory();
    private MailSenderService mailSenderService = mock(MailSenderService.class);
    private SessionService sessionService = mock(SessionService.class);
    private HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
    private TokenVerificationService mockFbVerifier = mock(TokenVerificationService.class);

    private RegistrationRepository registrationRepoSpy = spy(dummyRegistrationRepo);
    private RegistrationFactory registrationFactorySpy = spy(dummyRegistrationFactory);
    private UserRepository userRepoSpy = spy(dummyUserRepo);
    private UserFactory userFactorySpy = spy(dummyUserFactory);
    private IdentityRepository identityRepoSpy = spy(dummyIdentityRepo);
    private IdentityFactory identityFactorySpy = spy(dummyIdentityFactory);

    private UserServiceImpl service = new UserServiceImpl(mockFbVerifier, sessionService, userRepoSpy, userFactorySpy, identityRepoSpy, identityFactorySpy, registrationRepoSpy, registrationFactorySpy, new FilePropertiesProvider(getFileStream("test.testprops")));

    private String emailBody = "Hi <b>John Doe</b>,<br/><br/>Thank you for registering to Pings! To complete your registration, please verify your email by clicking on the following link:<br/>https://pings.com/signin.html?registration=";


    private InputStream getFileStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    @After
    public void tearDown() {
        dummyUserRepo.getStoredData().clear();
    }

    @Before
    public void setUp() throws Exception {
        User user = dummyUserFactory.createUser("pre-exist@email.com", "Existing user");
        dummyUserRepo.add(user);
    }

    @Test
    public void testCheckEmailUsageUsed() throws Exception {
        CheckEmailResponse actualResponse = service.checkEmailUsage("pre-exist@email.com");

        verify(userRepoSpy, times(1)).retrieve("pre-exist@email.com");
        assertTrue(actualResponse.isEmailUsed());
    }

    @Test
    public void testCheckEmailUsageUnused() throws Exception {
        CheckEmailResponse actualResponse = service.checkEmailUsage("non-exist@email.com");

        verify(userRepoSpy, times(1)).retrieve("non-exist@email.com");
        assertFalse(actualResponse.isEmailUsed());
    }


    @Test
    public void testCreateRegistration() throws Exception {
        service.setMailSender(mailSenderService, "emailVerification.html");
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@email");
        request.setDisplayName("John Doe");
        request.setPassword("encrypted-password");
        ArgumentCaptor<Registration> registrationCaptor = ArgumentCaptor.forClass(Registration.class);
        doNothing().when(mailSenderService).sendMail(anyString(), anyString(), anyString(), anyString());

        service.createRegistration(request);

        Registration reg = dummyRegistrationRepo.getStoredData().values().iterator().next();
        verify(mailSenderService, times(1)).sendMail("test@email", "John Doe", "Registration Confirmation", emailBody + reg.getId());
        verify(registrationFactorySpy, times(1)).createRegistration(eq("test@email"), eq("John Doe"), anyString());
        verify(registrationRepoSpy, times(1)).add(registrationCaptor.capture());

        Registration captured = registrationCaptor.getValue();
        assertEquals(request.getDisplayName(), captured.getDisplayName());
        assertTrue(BCrypt.checkpw("encrypted-password", captured.getPassword()));
        assertEquals(request.getEmail(), captured.getEmail());
    }

    @Test
    public void testVerifyUser() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("password", "pass");

        Registration registration = dummyRegistrationFactory.createRegistration("test@email", "John Doe", "pass");
        dummyRegistrationRepo.add(registration);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Identity> identityCaptor = ArgumentCaptor.forClass(Identity.class);

        service.verifyUser(registration.getId());

        verify(registrationRepoSpy, times(1)).retrieve(registration.getId());
        verify(userFactorySpy, times(1)).createUser("test@email", "John Doe");
        verify(userRepoSpy, times(1)).add(userCaptor.capture());
        User userCaptured = userCaptor.getValue();
        verify(identityFactorySpy, times(1)).createIdentity(isA(User.class), eq("self"), eq("test@email"));
        verify(identityRepoSpy, times(1)).add(identityCaptor.capture());
        Identity identityCaptured = identityCaptor.getValue();
        verify(registrationRepoSpy, times(1)).delete(registration);

        assertEquals("John Doe", userCaptured.getDisplayName());
        assertEquals("test@email", userCaptured.getId());
        assertEquals("self", identityCaptured.getProvider());
        assertEquals("test@email", identityCaptured.getProvidedIdentity());
        assertEquals(data, identityCaptured.getData());
        assertEquals(userCaptured, identityCaptured.getUser());
    }

    @Test
    public void testAuthenticateBasicUser() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@email");
        request.setProvider("self");
        request.setToken("password");

        User user = dummyUserRepo.add(dummyUserFactory.createUser(request.getEmail(), "John Doe"));
        Identity identity = dummyIdentityFactory.createIdentity(user, "self", "test@email");
        identity.addData("password", BCrypt.hashpw("password", BCrypt.gensalt()));
        dummyIdentityRepo.add(identity);
        when(sessionService.generateSession(eq(user), same(mockHttpRequest))).thenReturn("token");

        AuthenticationResponse response = service.authenticateBasicUser(request, mockHttpRequest);

        verify(identityRepoSpy, times(1)).findIdentityFromProvider("test@email", request.getProvider());
        verify(sessionService, times(1)).generateSession(eq(user), same(mockHttpRequest));
        assertEquals("token", response.getToken());
        assertEquals("self", response.getIdentityProvider());
    }

    @Test
    public void testAuthenticateBasicUserFailure() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@email");
        request.setProvider("self");
        request.setToken("derpword");

        User user = dummyUserRepo.add(dummyUserFactory.createUser(request.getEmail(), "John Doe"));
        Identity identity = dummyIdentityFactory.createIdentity(user, "self", "test@email");
        identity.addData("password", BCrypt.hashpw("password", BCrypt.gensalt()));
        dummyIdentityRepo.add(identity);

        AuthenticationResponse response = service.authenticateBasicUser(request, mockHttpRequest);

        verify(identityRepoSpy, times(1)).findIdentityFromProvider("test@email", request.getProvider());
        verify(sessionService, times(0)).generateSession(any(User.class), any(HttpServletRequest.class));
        assertNull(response.getToken());
    }

    @Test
    public void testAuthenticateFacebookUserFailure() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("id", "FB_ID");
        data.put("name", "John Doe");

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@email");
        request.setProvider("facebook");
        request.setToken("facebook_token");
        request.setData(data);
        when(mockFbVerifier.verifyToken("facebook_token", "FB_ID")).thenReturn(false);

        AuthenticationResponse response = service.authenticateFacebookUser(request, mockHttpRequest);

        verify(mockFbVerifier, times(1)).verifyToken("facebook_token", "FB_ID");
        verify(userFactorySpy, times(0)).createUser(anyString(), anyString());
        verify(userRepoSpy, times(0)).add(any(User.class));
        verify(identityFactorySpy, times(0)).createIdentity(any(User.class), anyString(), anyString());
        verify(identityRepoSpy, times(0)).add(any(Identity.class));
        verify(sessionService, times(0)).generateSession(any(User.class), any(HttpServletRequest.class));

        assertNull(response.getToken());
        assertEquals("facebook", response.getIdentityProvider());
    }

    @Test
    public void testAuthenticateFacebookUserNewUser() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("id", "FB_ID");
        data.put("name", "John Doe");

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@email");
        request.setProvider("facebook");
        request.setToken("facebook_token");
        request.setData(data);
        when(mockFbVerifier.verifyToken("facebook_token", "FB_ID")).thenReturn(true);
        when(sessionService.generateSession(any(User.class), same(mockHttpRequest))).thenReturn("token");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Identity> identityCaptor = ArgumentCaptor.forClass(Identity.class);

        AuthenticationResponse response = service.authenticateFacebookUser(request, mockHttpRequest);

        verify(mockFbVerifier, times(1)).verifyToken("facebook_token", "FB_ID");
        verify(userFactorySpy, times(1)).createUser("test@email", "John Doe");
        verify(userRepoSpy, times(1)).add(userCaptor.capture());
        User userCaptured = userCaptor.getValue();
        verify(identityFactorySpy, times(1)).createIdentity(any(User.class), eq("facebook"), eq("FB_ID"));
        verify(identityRepoSpy, times(1)).add(identityCaptor.capture());
        Identity identityCaptured = identityCaptor.getValue();
        verify(sessionService, times(1)).generateSession(eq(userCaptured), same(mockHttpRequest));

        assertEquals("John Doe", userCaptured.getDisplayName());
        assertEquals("test@email", userCaptured.getId());
        assertEquals("facebook", identityCaptured.getProvider());
        assertEquals("FB_ID", identityCaptured.getProvidedIdentity());
        assertEquals(userCaptured, identityCaptured.getUser());
        assertEquals("token", response.getToken());
        assertEquals("facebook", response.getIdentityProvider());
    }

    @Test
    public void testAuthenticateFacebookUserNewIdentity() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("id", "FB_ID");
        data.put("name", "John Doe");

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@email");
        request.setProvider("facebook");
        request.setToken("facebook_token");
        request.setData(data);

        User user = dummyUserRepo.add(dummyUserFactory.createUser(request.getEmail(), "John Doe"));
        when(sessionService.generateSession(any(User.class), same(mockHttpRequest))).thenReturn("token");
        ArgumentCaptor<Identity> identityCaptor = ArgumentCaptor.forClass(Identity.class);
        when(mockFbVerifier.verifyToken("facebook_token", "FB_ID")).thenReturn(true);

        AuthenticationResponse response = service.authenticateFacebookUser(request, mockHttpRequest);

        verify(userRepoSpy, times(1)).retrieve(request.getEmail());
        verify(userFactorySpy, times(0)).createUser(anyString(), anyString());
        verify(userRepoSpy, times(0)).add(any(User.class));
        verify(identityFactorySpy, times(1)).createIdentity(any(User.class), eq("facebook"), eq("FB_ID"));
        verify(identityRepoSpy, times(1)).add(identityCaptor.capture());
        Identity identityCaptured = identityCaptor.getValue();
        verify(sessionService, times(1)).generateSession(eq(user), same(mockHttpRequest));

        assertEquals("facebook", identityCaptured.getProvider());
        assertEquals("FB_ID", identityCaptured.getProvidedIdentity());
        assertEquals(user, identityCaptured.getUser());
        assertEquals("token", response.getToken());
        assertEquals("facebook", response.getIdentityProvider());
    }

    @Test
    public void testAuthenticateFacebookUserExisting() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("id", "FB_ID");
        data.put("name", "John Doe");

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@email");
        request.setProvider("facebook");
        request.setToken("facebook_token");
        request.setData(data);

        User user = dummyUserRepo.add(dummyUserFactory.createUser(request.getEmail(), "John Doe"));
        Identity identity = dummyIdentityRepo.add(dummyIdentityFactory.createIdentity(user, "facebook", "FB_ID"));
        when(sessionService.generateSession(any(User.class), same(mockHttpRequest))).thenReturn("token");
        when(mockFbVerifier.verifyToken("facebook_token", "FB_ID")).thenReturn(true);

        AuthenticationResponse response = service.authenticateFacebookUser(request, mockHttpRequest);

        verify(userRepoSpy, times(0)).retrieve(request.getEmail());
        verify(userFactorySpy, times(0)).createUser(anyString(), anyString());
        verify(userRepoSpy, times(0)).add(any(User.class));
        verify(identityRepoSpy, times(1)).findIdentityFromProvider("FB_ID", "facebook");
        verify(identityFactorySpy, times(0)).createIdentity(any(User.class), anyString(), anyString());
        verify(identityRepoSpy, times(0)).add(any(Identity.class));
        verify(sessionService, times(1)).generateSession(eq(user), same(mockHttpRequest));
        assertEquals("token", response.getToken());
        assertEquals("facebook", response.getIdentityProvider());
    }

    @Test
    public void testAuthenticateGoogleUserNew() throws Exception {

    }

    @Test
    public void testAuthenticateGoogleUserExisting() throws Exception {

    }

}