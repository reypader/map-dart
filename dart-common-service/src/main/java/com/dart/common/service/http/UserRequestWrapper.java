package com.dart.common.service.http;

import com.dart.data.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

/**
 * @author RMPader
 */
public class UserRequestWrapper extends HttpServletRequestWrapper {


    private final HttpServletRequest realRequest;
    private final UserPrincipal principal;

    public UserRequestWrapper(User user, HttpServletRequest request) {
        super(request);
        this.principal = new UserPrincipal(user);
        this.realRequest = request;
    }

    @Override
    public Principal getUserPrincipal() {
        if (this.principal.getUser() == null) {
            return realRequest.getUserPrincipal();
        }
        return principal;
    }
}