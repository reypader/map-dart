package com.dart.user.api;

/**
 * @author RMPader
 */
public class RecaptchaResponse {

    private boolean userIsHuman;

    public boolean isUserIsHuman() {
        return userIsHuman;
    }

    public void setUserIsHuman(boolean userIsHuman) {
        this.userIsHuman = userIsHuman;
    }

}
