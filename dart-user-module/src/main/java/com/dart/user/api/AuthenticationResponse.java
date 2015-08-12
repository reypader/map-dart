package com.dart.user.api;

/**
 * @author RMPader
 */
public class AuthenticationResponse {
    private String token;
    private String identityProvider;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }
}
