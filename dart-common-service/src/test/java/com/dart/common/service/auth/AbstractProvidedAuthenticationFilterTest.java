package com.dart.common.service.auth;

import com.dart.common.test.domain.DummyIdentity;
import com.dart.common.test.domain.DummyUser;
import com.dart.data.domain.Identity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by rpader on 4/28/16.
 */
public class AbstractProvidedAuthenticationFilterTest {

    private HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
    private HttpServletResponse mockHttpResponse = mock(HttpServletResponse.class);
    private TokenVerificationService mockVerificationService = mock(TokenVerificationService.class);
    private AbstractProvidedAuthenticationFilter abstractProvidedAuthenticationFilter;

    @Before
    public void setup() {
        abstractProvidedAuthenticationFilter = new AbstractProvidedAuthenticationFilter(
                "url", "test",
                mockVerificationService) {
            @Override
            protected Identity fetchIdentity(HttpServletRequest request, String providedIdentity, String provider) {
                DummyUser dummyUser = new DummyUser();
                dummyUser.setId("dummy");
                dummyUser.setEmail("dummy@email.com");

                DummyIdentity dummyIdentity = new DummyIdentity();
                dummyIdentity.setId("dummyId");
                dummyIdentity.setProvidedIdentity("id");
                dummyIdentity.setUser(dummyUser);
                return dummyIdentity;
            }
        };

        when(mockHttpRequest.getParameter("identity")).thenReturn("id");
        when(mockHttpRequest.getParameter("token")).thenReturn("token");
    }

    @Test
    public void testAttemptAuthenticationSuccess() throws Exception {
        when(mockVerificationService.verifyToken(eq("token"), eq("id"))).thenReturn(true);
        Authentication authentication = abstractProvidedAuthenticationFilter.attemptAuthentication(mockHttpRequest,
                                                                                                   mockHttpResponse);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        assertEquals("dummy@email.com", principal.getUsername());
        assertEquals("id", principal.getPassword());
        assertEquals(Collections.singletonList(new SimpleGrantedAuthority("USER")), principal.getAuthorities());
    }

    @Test(expected = BadCredentialsException.class)
    public void testAttemptAuthenticationFail() throws Exception {
        when(mockVerificationService.verifyToken(eq("token"), eq("id"))).thenReturn(false);

        abstractProvidedAuthenticationFilter.attemptAuthentication(mockHttpRequest, mockHttpResponse);
    }

    @Test(expected = AuthenticationException.class)
    public void testAttemptAuthenticationError() throws Exception {
        when(mockVerificationService.verifyToken(eq("token"), eq("id"))).thenThrow(new Exception());
        abstractProvidedAuthenticationFilter.attemptAuthentication(mockHttpRequest, mockHttpResponse);
    }
}