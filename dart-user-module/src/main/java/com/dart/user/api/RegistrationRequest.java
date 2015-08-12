package com.dart.user.api;

/**
 * @author RMPader
 */
public class RegistrationRequest {
    private String email;
    private String displayName;
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
