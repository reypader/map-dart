package com.dart.common.service.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author RMPader
 */
@Component
@ConfigurationProperties(prefix = "app")
public class PropertiesProvider {

    private String name;
    private String appId;
    private String serviceAccount;
    private String cloudStorageURL;
    private String cloudStorageBucket;
    private ThirdPartyApi gplus;
    private ThirdPartyApi facebook;
    private ThirdPartyApi recaptcha;
    private String userWebsiteURL;
    private String signupEmailTemplate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(String serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public String getCloudStorageURL() {
        return cloudStorageURL;
    }

    public void setCloudStorageURL(String cloudStorageURL) {
        this.cloudStorageURL = cloudStorageURL;
    }

    public String getCloudStorageBucket() {
        return cloudStorageBucket;
    }

    public void setCloudStorageBucket(String cloudStorageBucket) {
        this.cloudStorageBucket = cloudStorageBucket;
    }

    public ThirdPartyApi getGplus() {
        return gplus;
    }

    public void setGplus(ThirdPartyApi gplus) {
        this.gplus = gplus;
    }

    public ThirdPartyApi getFacebook() {
        return facebook;
    }

    public void setFacebook(ThirdPartyApi facebook) {
        this.facebook = facebook;
    }

    public ThirdPartyApi getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(ThirdPartyApi recaptcha) {
        this.recaptcha = recaptcha;
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

    public static class ThirdPartyApi {

        private String appId;
        private String secret;
        private String endpoint;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }
}
