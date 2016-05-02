package com.dart.user.service;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.exception.IllegalTransactionException;
import com.dart.common.service.mail.MailSenderService;
import com.dart.common.test.factory.DummyIdentityFactory;
import com.dart.common.test.factory.DummyRegistrationFactory;
import com.dart.common.test.factory.DummyUserFactory;
import com.dart.common.service.properties.FilePropertiesProvider;
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
import com.dart.user.api.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
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
    private HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
    private TokenVerificationService mockRecaptchaVerifier = mock(TokenVerificationService.class);

    private RegistrationRepository registrationRepoSpy = spy(dummyRegistrationRepo);
    private RegistrationFactory registrationFactorySpy = spy(dummyRegistrationFactory);
    private UserRepository userRepoSpy = spy(dummyUserRepo);
    private UserFactory userFactorySpy = spy(dummyUserFactory);
    private IdentityRepository identityRepoSpy = spy(dummyIdentityRepo);
    private IdentityFactory identityFactorySpy = spy(dummyIdentityFactory);

    private UserServiceImpl service;

    private String emailBody = "Hi <b>John Doe</b>,<br/><br/>Thank you for registering to Pings! To complete your registration, please verify your email by clicking on the following link:<br/>https://pings.com/signin.html?registration=";
    private Date later;

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
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 1);
        later = now.getTime();
        User user = dummyUserFactory.createUser("pre-exist@email.com", "Existing user");
        dummyUserRepo.add(user);
        service = new UserServiceImpl(mockRecaptchaVerifier, userRepoSpy,
                                      userFactorySpy, registrationRepoSpy, registrationFactorySpy, identityRepoSpy,
                                      identityFactorySpy,
                                      new FilePropertiesProvider(getFileStream("test.testprops")), mailSenderService);
    }

    @Test
    public void testCheckEmailUsageUsed() throws Exception {
        CheckEmailResponse actualResponse = service.checkEmailUsage("pre-exist@email.com");

        verify(userRepoSpy, times(1)).retrieveByEmail("pre-exist@email.com");
        assertTrue(actualResponse.isEmailUsed());
    }

    @Test
    public void testCheckEmailUsageUnused() throws Exception {
        CheckEmailResponse actualResponse = service.checkEmailUsage("non-exist@email.com");

        verify(userRepoSpy, times(1)).retrieveByEmail("non-exist@email.com");
        assertFalse(actualResponse.isEmailUsed());
    }


    @Test
    public void testCreateRegistration() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@email");
        request.setDisplayName("John Doe");
        request.setPassword("encrypted-password");
        ArgumentCaptor<Registration> registrationCaptor = ArgumentCaptor.forClass(Registration.class);
        doNothing().when(mailSenderService).sendMail(anyString(), anyString(), anyString(), anyString());

        service.createRegistration(request);

        Registration reg = dummyRegistrationRepo.getStoredData().values().iterator().next();
        verify(mailSenderService, times(1)).sendMail("test@email", "John Doe", "Registration Confirmation",
                                                     emailBody + reg.getId());
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

        VerificationResponse response = service.verifyUser(registration.getId());

        verify(registrationRepoSpy, times(1)).retrieve(registration.getId());
        verify(userFactorySpy, times(1)).createUser("test@email", "John Doe");
        verify(userRepoSpy, times(1)).add(userCaptor.capture());
        User userCaptured = userCaptor.getValue();
        verify(identityFactorySpy, times(1)).createIdentity(isA(User.class), eq("basic"), eq("test@email"));
        verify(identityRepoSpy, times(1)).add(identityCaptor.capture());
        Identity identityCaptured = identityCaptor.getValue();
        verify(registrationRepoSpy, times(1)).deleteRegistrationForEmail("test@email");

        assertEquals("John Doe", userCaptured.getDisplayName());
        assertEquals("test@email", userCaptured.getEmail());
        assertEquals("basic", identityCaptured.getProvider());
        assertEquals("test@email", identityCaptured.getProvidedIdentity());
        assertEquals(data, identityCaptured.getData());
        assertEquals(userCaptured, identityCaptured.getUser());
        assertFalse(response.isError());
    }

    @Test
    public void testVerifyInvalidRegistration() throws Exception {
        VerificationResponse response = service.verifyUser("derp");

        verify(registrationRepoSpy, times(1)).retrieve("derp");
        verify(userFactorySpy, times(0)).createUser(anyString(), anyString());
        verify(userRepoSpy, times(0)).add(any(User.class));
        verify(identityFactorySpy, times(0)).createIdentity(any(User.class), anyString(), anyString());
        verify(identityRepoSpy, times(0)).add(any(Identity.class));
        verify(registrationRepoSpy, times(0)).deleteRegistrationForEmail(anyString());
        assertTrue(response.isError());
    }

    @Test
    public void testVerifyExistingUser() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("password", "pass");

        Registration registration = dummyRegistrationFactory.createRegistration("pre-exist@email.com", "John Doe",
                                                                                "pass");
        dummyRegistrationRepo.add(registration);

        VerificationResponse response = service.verifyUser(registration.getId());

        verify(registrationRepoSpy, times(1)).retrieve(registration.getId());
        verify(userRepoSpy, times(1)).retrieveByEmail("pre-exist@email.com");
        verify(userFactorySpy, times(0)).createUser("pre-exist@email.com", "John Doe");
        verify(userRepoSpy, times(0)).add(any(User.class));
        verify(identityFactorySpy, times(0)).createIdentity(any(User.class), anyString(), anyString());
        verify(identityRepoSpy, times(0)).add(any(Identity.class));
        verify(registrationRepoSpy, times(1)).deleteRegistrationForEmail("pre-exist@email.com");
        assertTrue(response.isError());
    }

    @Test
    public void testValidateRecaptchaResult() throws Exception {
        when(mockRecaptchaVerifier.verifyToken("recaptcha", "127.0.0.1")).thenReturn(true);
        when(mockHttpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        RecaptchaRequest request = new RecaptchaRequest();
        request.setRecaptchaResult("recaptcha");

        RecaptchaResponse response = service.validateRecaptchaResult(request, mockHttpRequest);

        verify(mockRecaptchaVerifier, times(1)).verifyToken("recaptcha", "127.0.0.1");
        assertTrue(response.isUserIsHuman());
    }

    @Test
    public void testValidateRecaptchaResultFail() throws Exception {
        when(mockRecaptchaVerifier.verifyToken("recaptcha", "127.0.0.1")).thenReturn(false);
        when(mockHttpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        RecaptchaRequest request = new RecaptchaRequest();
        request.setRecaptchaResult("recaptcha");

        RecaptchaResponse response = service.validateRecaptchaResult(request, mockHttpRequest);

        verify(mockRecaptchaVerifier, times(1)).verifyToken("recaptcha", "127.0.0.1");
        assertFalse(response.isUserIsHuman());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = dummyUserFactory.createUser("test@email", "John Doe");
        dummyUserRepo.add(user);
        UpdateUserRequest request = new UpdateUserRequest();
        request.setDisplayName("Derp");
        request.setDescription("Describe me");
        request.setPhotoURL("http://url.com");
        request.setId(user.getId());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        service.updateUser(request, user);

        verify(userRepoSpy, times(1)).update(userCaptor.capture());
        User userCaptured = userCaptor.getValue();
        assertEquals(user.getId(), userCaptured.getId());
        assertEquals(user.getEmail(), userCaptured.getEmail());
        assertEquals("Derp", userCaptured.getDisplayName());
        assertEquals("Describe me", userCaptured.getDescription());
        assertEquals("http://url.com", userCaptured.getPhotoURL());
        assertEquals(user.getSecret(), userCaptured.getSecret());
    }

    @Test(expected = IllegalTransactionException.class)
    public void testUpdateUserFraud() throws Exception {
        User user = dummyUserFactory.createUser("test@email", "John Doe");
        UpdateUserRequest request = new UpdateUserRequest();
        request.setDisplayName("Derp");
        request.setDescription("Describe me");
        request.setPhotoURL("http://url.com");
        request.setId("412341");

        service.updateUser(request, user);
        verify(userRepoSpy, times(0)).update(any(User.class));
    }

    @Test(expected = IllegalTransactionException.class)
    public void testUpdateNonExistentUser() throws Exception {
        User user = dummyUserFactory.createUser("test@email", "John Doe");
        UpdateUserRequest request = new UpdateUserRequest();
        request.setDisplayName("Derp");
        request.setDescription("Describe me");
        request.setPhotoURL("http://url.com");
        request.setId(user.getId());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        service.updateUser(request, user);
    }

}