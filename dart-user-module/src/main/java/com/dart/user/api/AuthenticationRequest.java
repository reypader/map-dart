package com.dart.user.api;

import java.util.Map;

/**
 * @author RMPader
 */
public class AuthenticationRequest {
    private String email;
    private String provider;
    private String token;
    private Map<String, String> data;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }
}
