package com.dart.common.service.aop;

import com.dart.common.service.http.UserPrincipal;
import com.dart.common.service.http.exception.UnauthorizedException;
import com.dart.data.domain.User;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author RMPader
 */
public class AuthenticationMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?>[] params = invocation.getMethod().getParameterTypes();
        int requestIndex = getIndex(HttpServletRequest.class, params);
        if (requestIndex >= 0) {
            Object[] args = invocation.getArguments();
            HttpServletRequest request = (HttpServletRequest) args[requestIndex];
            Principal p = request.getUserPrincipal();
            if (p instanceof UserPrincipal) {
                UserPrincipal principal = (UserPrincipal) p;
                User user = principal.getUser();
                if (user == null) {
                    throw new UnauthorizedException("Authorization may have failed.");
                } else {
                    return invocation.proceed();
                }
            } else {
                throw new UnauthorizedException("User was not authorized. Authorization header is not present or UserAuthorizationFilter in the filter chain");
            }
        } else {
            throw new IllegalStateException("Method annotated with @Authenticated needs to have one HttpServletRequest parameter");
        }
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
