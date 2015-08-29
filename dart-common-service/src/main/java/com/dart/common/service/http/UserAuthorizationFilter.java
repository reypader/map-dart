package com.dart.common.service.http;

import com.dart.common.service.auth.HttpRequestAuthorizationService;
import com.dart.data.domain.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author RMPader
 */
@Singleton
public class UserAuthorizationFilter implements Filter {

    private final HttpRequestAuthorizationService authorizationService;

    @Inject
    public UserAuthorizationFilter(HttpRequestAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void init(FilterConfig cfg) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain next) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        User user;
        String authHeader = request.getHeader("authorization");
        if (authHeader != null && !authHeader.equals("")) {
            user = authorizationService.verifyToken(request);
            next.doFilter(new UserRequestWrapper(user, request), response);
        } else {
            next.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}