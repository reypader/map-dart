package com.dart.common.service.http;

import com.dart.common.service.auth.HttpRequestAuthorizationService;
import com.dart.common.test.domain.DummyUser;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author RMPader
 */
public class UserAuthorizationFilterTest {

    private HttpRequestAuthorizationService mockAuthorizationService = mock(HttpRequestAuthorizationService.class);
    private Filter filter = new UserAuthorizationFilter(mockAuthorizationService);
    private HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);

    @Test
    public void testDoFilter() throws Exception {
        when(mockHttpRequest.getHeader("authorization")).thenReturn("TEST");
        when(mockAuthorizationService.verifyToken(mockHttpRequest)).thenReturn(new DummyUser());
        filter.doFilter(mockHttpRequest, null, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                assertTrue(request instanceof UserRequestWrapper);
            }
        });

        verify(mockHttpRequest, times(1)).getHeader("authorization");
        verify(mockAuthorizationService, times(1)).verifyToken(mockHttpRequest);
    }

    @Test
    public void testDoFilterNoAuth() throws Exception {
        when(mockHttpRequest.getHeader("authorization")).thenReturn(null);
        filter.doFilter(mockHttpRequest, null, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                assertEquals(mockHttpRequest, request);
            }
        });

        verify(mockHttpRequest, times(1)).getHeader("authorization");
        verify(mockAuthorizationService, times(0)).verifyToken(any(HttpServletRequest.class));
    }
}