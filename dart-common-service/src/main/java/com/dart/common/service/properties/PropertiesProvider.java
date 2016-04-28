package com.dart.common.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author RMPader
 */
@Component
@ConfigurationProperties(prefix = "app")
public class PropertiesProvider {

    private String appName;
    private String googleAppId;
    private String googleServiceAccount;
    private String googleCloudStorageURL;
    private String googleCloudStorageBucket;
    private String gplusAppId;
    private String gplusAppSecret;
    private String gplusEndpoint;
    private String facebookAppId;
    private String facebookSecret;
    private String facebookEndpoint;
    private String recaptchaEndpoint;
    private String recaptchaSecret;
    private String userWebsiteURL;
    private String signupEmailTemplate;

    public String getGoogleAppId() {
        return googleAppId;
    }

    public void setGoogleAppId(String googleAppId) {
        this.googleAppId = googleAppId;
    }

    public String getGoogleServiceAccount() {
        return googleServiceAccount;
    }

    public void setGoogleServiceAccount(String googleServiceAccount) {
        this.googleServiceAccount = googleServiceAccount;
    }

    public String getGoogleCloudStorageURL() {
        return googleCloudStorageURL;
    }

    public void setGoogleCloudStorageURL(String googleCloudStorageURL) {
        this.googleCloudStorageURL = googleCloudStorageURL;
    }

    public String getGoogleCloudStorageBucket() {
        return googleCloudStorageBucket;
    }

    public void setGoogleCloudStorageBucket(String googleCloudStorageBucket) {
        this.googleCloudStorageBucket = googleCloudStorageBucket;
    }

    public String getGplusAppId() {
        return gplusAppId;
    }

    public void setGplusAppId(String gplusAppId) {
        this.gplusAppId = gplusAppId;
    }

    public String getGplusAppSecret() {
        return gplusAppSecret;
    }

    public void setGplusAppSecret(String gplusAppSecret) {
        this.gplusAppSecret = gplusAppSecret;
    }

    public String getGplusEndpoint() {
        return gplusEndpoint;
    }

    public void setGplusEndpoint(String gplusEndpoint) {
        this.gplusEndpoint = gplusEndpoint;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public String getFacebookSecret() {
        return facebookSecret;
    }

    public void setFacebookSecret(String facebookSecret) {
        this.facebookSecret = facebookSecret;
    }

    public String getFacebookEndpoint() {
        return facebookEndpoint;
    }

    public void setFacebookEndpoint(String facebookEndpoint) {
        this.facebookEndpoint = facebookEndpoint;
    }

    public String getRecaptchaEndpoint() {
        return recaptchaEndpoint;
    }

    public void setRecaptchaEndpoint(String recaptchaEndpoint) {
        this.recaptchaEndpoint = recaptchaEndpoint;
    }

    public String getRecaptchaSecret() {
        return recaptchaSecret;
    }

    public void setRecaptchaSecret(String recaptchaSecret) {
        this.recaptchaSecret = recaptchaSecret;
    }

    public String getUserWebsiteURL() {
        return userWebsiteURL;
    }

    public void setUserWebsiteURL(String userWebsiteURL) {
        this.userWebsiteURL = userWebsiteURL;
    }

    public String getSignupEmailTemplate() {
        return signupEmailTemplate;
    }

    public void setSignupEmailTemplate(String signupEmailTemplate) {
        this.signupEmailTemplate = signupEmailTemplate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
