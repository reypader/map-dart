package com.dart.common.service.auth;

import com.dart.common.service.properties.FilePropertiesProvider;
import com.dart.common.test.factory.DummyUserFactory;
import com.dart.common.test.repository.DummyUserRepository;
import com.dart.data.domain.User;
import com.dart.data.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author RMPader
 */
public class JwtHttpRequestAuthorizationServiceTest {

    private DummyUserRepository dummyUserRepo = new DummyUserRepository();
    private UserRepository userRepoSpy = spy(dummyUserRepo);

    private HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);

    private HttpRequestAuthorizationService service = new JwtHttpRequestAuthorizationService(userRepoSpy, new FilePropertiesProvider(getFileStream("test.testprops")));
    private User user = new DummyUserFactory().createUser("test@email", "John Doe");

    private InputStream getFileStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    @Before
    public void setup() {
        dummyUserRepo.add(user);
    }

    @After
    public void teardown() {
        dummyUserRepo.delete(user);
    }

    @Test
    public void testLegitimateToken() throws Exception {
        Calendar later = Calendar.getInstance();
        later.add(Calendar.DAY_OF_YEAR, 30);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.0.1,127.0.0.1");

        String token = service.generateToken(later.getTime(), user, mockHttpRequest);

        when(mockHttpRequest.getHeader("AUTHORIZATION")).thenReturn(user.getId() + " " + token);
        assertEquals(user, service.verifyToken(mockHttpRequest));
        verify(userRepoSpy, times(1)).retrieve(user.getId());
    }

    @Test
    public void testStolenToken() {
        Calendar later = Calendar.getInstance();
        later.add(Calendar.DAY_OF_YEAR, 30);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.0.1,127.0.0.1");

        String token = service.generateToken(later.getTime(), user, mockHttpRequest);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("127.0.0.1");

        when(mockHttpRequest.getHeader("AUTHORIZATION")).thenReturn(user.getId() + " " + token);
        assertNull(service.verifyToken(mockHttpRequest));
        verify(userRepoSpy, times(1)).retrieve(user.getId());
    }

    @Test
    public void testFalseUser() {
        dummyUserRepo.add(new DummyUserFactory().createUser("test2@email", "John Doe"));
        Calendar later = Calendar.getInstance();
        later.add(Calendar.DAY_OF_YEAR, 30);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.0.1,127.0.0.1");

        String token = service.generateToken(later.getTime(), user, mockHttpRequest);

        when(mockHttpRequest.getHeader("AUTHORIZATION")).thenReturn("test2@email " + token);
        assertNull(service.verifyToken(mockHttpRequest));
        verify(userRepoSpy, times(1)).retrieve("test2@email");
    }

    @Test
    public void testWhatUser() {
        Calendar later = Calendar.getInstance();
        later.add(Calendar.DAY_OF_YEAR, 30);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.0.1,127.0.0.1");

        String token = service.generateToken(later.getTime(), user, mockHttpRequest);

        when(mockHttpRequest.getHeader("AUTHORIZATION")).thenReturn("test2@email " + token);
        assertNull(service.verifyToken(mockHttpRequest));
        verify(userRepoSpy, times(1)).retrieve("test2@email");
    }
}