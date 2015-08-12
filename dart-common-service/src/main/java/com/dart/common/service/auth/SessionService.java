package com.dart.common.service.auth;

import com.dart.data.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author RMPader
 */
public interface SessionService {

    String generateSession(User user, HttpServletRequest request);

    boolean verifySession(User user, HttpServletRequest request);

    void invalidateSession(String sessionId);
}
