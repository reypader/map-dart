package com.dart.common.service.auth;

import com.dart.common.service.properties.FilePropertiesProvider;
import com.dart.common.test.factory.DummyUserFactory;
import com.dart.data.domain.User;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author RMPader
 */
public class JwtAuthenticationTokenServiceTest {

    private HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);

    private AuthenticationTokenService service = new JwtAuthenticationTokenService(new FilePropertiesProvider(getFileStream("test.testprops")));
    private User user = new DummyUserFactory().createUser("test@email", "John Doe");

    private InputStream getFileStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }


    @Test
    public void testLegitimateToken() throws Exception {
        Calendar later = Calendar.getInstance();
        later.add(Calendar.DAY_OF_YEAR, 30);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.0.1,127.0.0.1");

        String token = service.generateToken(later.getTime(), user, mockHttpRequest);

        when(mockHttpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        assertTrue(service.verifyToken(user, mockHttpRequest));
    }

    @Test
    public void testStolenToken() {
        Calendar later = Calendar.getInstance();
        later.add(Calendar.DAY_OF_YEAR, 30);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.0.1,127.0.0.1");

        String token = service.generateToken(later.getTime(), user, mockHttpRequest);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("127.0.0.1");

        when(mockHttpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        assertFalse(service.verifyToken(user, mockHttpRequest));
    }

    @Test
    public void testFalseUser() {
        Calendar later = Calendar.getInstance();
        later.add(Calendar.DAY_OF_YEAR, 30);
        when(mockHttpRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.0.1,127.0.0.1");

        String token = service.generateToken(later.getTime(), user, mockHttpRequest);

        when(mockHttpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        assertFalse(service.verifyToken(new DummyUserFactory().createUser("test2@email", "John Doe"), mockHttpRequest));
    }
}