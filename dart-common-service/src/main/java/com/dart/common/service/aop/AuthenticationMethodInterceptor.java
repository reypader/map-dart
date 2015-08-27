package com.dart.common.service.aop;

import com.dart.common.service.auth.HttpRequestAuthorizationService;
import com.dart.data.domain.User;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
public class AuthenticationMethodInterceptor implements MethodInterceptor {


    private final HttpRequestAuthorizationService httpRequestAuthorizationService;

    public AuthenticationMethodInterceptor(HttpRequestAuthorizationService httpRequestAuthorizationService) {
        this.httpRequestAuthorizationService = httpRequestAuthorizationService;

    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?>[] params = invocation.getMethod().getParameterTypes();
        int userIndex = getIndex(User.class, params);
        int requestIndex = getIndex(HttpServletRequest.class, params);
        Object[] args = invocation.getArguments();
        User user = httpRequestAuthorizationService.verifyToken((HttpServletRequest) args[requestIndex]);
        args[userIndex] = user;
        return invocation.proceed();
    }

    private int getIndex(Class<?> clazs, Class<?>[] params) {
        for (int i = 0; i < params.length; i++) {
            Class<?> clazz = params[i];
            if (clazz.equals(clazs)) {
                return i;
            }
        }
        return -1;
    }
}
