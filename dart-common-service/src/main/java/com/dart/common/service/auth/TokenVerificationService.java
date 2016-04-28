package com.dart.common.service.auth;

/**
 * Base class for verifying access tokens given by OAuth providers.
 *
 * @author RMPader
 */
public interface TokenVerificationService {

    /**
     * Check with the provider if the token is valid. When the provider accepts the token, information about the user
     * who authorized the token should match the passed identity.
     *
     * @param token    the token supposedly given by the provider.
     * @param identity the user's identity as it is known by the provider.
     * @return true if all the token is verified by the provider and the identities match.
     */
    boolean verifyToken(String token, String identity) throws Exception;
}
