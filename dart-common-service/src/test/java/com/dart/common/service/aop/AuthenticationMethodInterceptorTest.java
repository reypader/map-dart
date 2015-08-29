package com.dart.common.service.aop;

import com.dart.common.service.http.UserPrincipal;
import com.dart.common.service.http.exception.UnauthorizedException;
import com.dart.common.test.domain.DummyUser;
import com.dart.data.domain.User;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.security.Principal;

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
        final Method m = target.getClass().getDeclaredMethod("test", Object.class, HttpServletRequest.class);
        MethodInvocation invocation = new MethodInvocation() {
            private Object[] args = new Object[2];

            @Override
            public Method getMethod() {
                return m;
            }

            @Override
            public Object[] getArguments() {
                args[1] = mockHttpRequest;
                return args;
            }

            @Override
            public Object proceed() throws Throwable {
                return target.test(getArguments()[0], (HttpServletRequest) getArguments()[1]);
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
        when(mockHttpRequest.getUserPrincipal()).thenReturn(new UserPrincipal(dummyUser));

        AuthenticationMethodInterceptor auth = new AuthenticationMethodInterceptor();
        User u = (User) auth.invoke(invocation);

        assertEquals(dummyUser.getId(), u.getId());
    }

    @Test(expected = UnauthorizedException.class)
    public void testInvokeFail() throws Throwable {
        final SomeMethod target = new SomeMethod();
        final HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        final Method m = target.getClass().getDeclaredMethod("test", Object.class, HttpServletRequest.class);
        MethodInvocation invocation = new MethodInvocation() {
            private Object[] args = new Object[2];

            @Override
            public Method getMethod() {
                return m;
            }

            @Override
            public Object[] getArguments() {
                args[1] = mockHttpRequest;
                return args;
            }

            @Override
            public Object proceed() throws Throwable {
                return target.test(getArguments()[0], (HttpServletRequest) getArguments()[1]);
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
        when(mockHttpRequest.getUserPrincipal()).thenReturn(new Principal() {

            @Override
            public String getName() {
                return "fail";
            }
        });

        AuthenticationMethodInterceptor auth = new AuthenticationMethodInterceptor();
        auth.invoke(invocation);
    }
}