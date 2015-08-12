package com.dart.common.service.auth;

import java.io.IOException;

/**
 * @author RMPader
 */
public interface TokenVerificationService {
    boolean verifyToken(String token, String identity);
}
