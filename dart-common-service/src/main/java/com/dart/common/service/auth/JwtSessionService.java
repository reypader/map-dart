package com.dart.common.service.auth;

import com.dart.data.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
public class JwtSessionService implements SessionService {
    @Override
    public String generateSession(User user, HttpServletRequest request) {
        return "TOKEN";
    }

    @Override
    public boolean verifySession(User user, HttpServletRequest request) {
        return true;
    }

    @Override
    public void invalidateSession(String sessionId) {

    }
}
