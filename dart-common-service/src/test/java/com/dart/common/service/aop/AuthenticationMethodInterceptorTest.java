package com.dart.common.service.aop;

import com.dart.common.service.auth.HttpRequestAuthorizationService;
import com.dart.common.test.domain.DummyUser;
import com.dart.data.domain.User;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author RMPader
 */
public class AuthenticationMethodInterceptorTest {


    @Test
    public void testInvoke() throws Throwable {
        final SomeMethod target = new SomeMethod();
        final HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        final HttpRequestAuthorizationService mockAuthorizer = mock(HttpRequestAuthorizationService.class);
        final Method m = target.getClass().getDeclaredMethod("test", Object.class, User.class, Object.class, HttpServletRequest.class);
        MethodInvocation invocation = new MethodInvocation() {
            private Object[] args = new Object[4];
            @Override
            public Method getMethod() {
                return m;
            }

            @Override
            public Object[] getArguments() {
                args[3] = mockHttpRequest;
                return args;
            }

            @Override
            public Object proceed() throws Throwable {
                return target.test(getArguments()[0],(User)getArguments()[1],getArguments()[2],(HttpServletRequest)getArguments()[3]);
            }

            @Override
            public Object getThis() {
                return null;
            }

            @Override
            public AccessibleObject getStaticPart() {
                return null;
            }
        };
        DummyUser dummyUser = new DummyUser();
        dummyUser.setId("id");
        when(mockAuthorizer.verifyToken(mockHttpRequest)).thenReturn(dummyUser);

        AuthenticationMethodInterceptor auth = new AuthenticationMethodInterceptor(mockAuthorizer);
        User user = (User) auth.invoke(invocation);

        assertEquals(dummyUser.getId(), user.getId());
    }
}