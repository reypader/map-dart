package com.dart.common.service.auth;

import com.dart.data.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author RMPader
 */
public interface AuthenticationService {


    String generateToken(Date expiry, User user, HttpServletRequest request);

    boolean verifyToken(User user, HttpServletRequest request);

}
