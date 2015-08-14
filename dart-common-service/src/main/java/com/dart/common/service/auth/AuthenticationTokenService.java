package com.dart.common.service.auth;

import com.dart.data.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author RMPader
 */
public interface AuthenticationTokenService {

    String generateSession(Date expiry, User user, HttpServletRequest request);

    boolean verifySession(User user, HttpServletRequest request);

    void invalidateSession(String sessionId);
}
