package com.dart.user.service.property;

import com.dart.common.service.property.PropertiesProvider;

/**
 * Created by rpader on 5/2/16.
 */
public class TestPropertiesProvider extends PropertiesProvider {
    private PropertiesProvider app;

    public PropertiesProvider getApp() {
        return app;
    }

    public void setApp(PropertiesProvider app) {
        this.app = app;
    }

    @Override
    public String getName() {
        return app.getName();
    }

    @Override
    public void setName(String name) {
        app.setName(name);
    }

    @Override
    public String getAppId() {
        return app.getAppId();
    }

    @Override
    public void setAppId(String appId) {
        app.setAppId(appId);
    }

    @Override
    public String getServiceAccount() {
        return app.getServiceAccount();
    }

    @Override
    public void setServiceAccount(String serviceAccount) {
        app.setServiceAccount(serviceAccount);
    }

    @Override
    public String getCloudStorageURL() {
        return app.getCloudStorageURL();
    }

    @Override
    public void setCloudStorageURL(String cloudStorageURL) {
        app.setCloudStorageURL(cloudStorageURL);
    }

    @Override
    public String getCloudStorageBucket() {
        return app.getCloudStorageBucket();
    }

    @Override
    public void setCloudStorageBucket(String cloudStorageBucket) {
        app.setCloudStorageBucket(cloudStorageBucket);
    }

    @Override
    public ThirdPartyApi getGplus() {
        return app.getGplus();
    }

    @Override
    public void setGplus(ThirdPartyApi gplus) {
        app.setGplus(gplus);
    }

    @Override
    public ThirdPartyApi getFacebook() {
        return app.getFacebook();
    }

    @Override
    public void setFacebook(ThirdPartyApi facebook) {
        app.setFacebook(facebook);
    }

    @Override
    public ThirdPartyApi getRecaptcha() {
        return app.getRecaptcha();
    }

    @Override
    public void setRecaptcha(ThirdPartyApi recaptcha) {
        app.setRecaptcha(recaptcha);
    }

    @Override
    public String getUserWebsiteURL() {
        return app.getUserWebsiteURL();
    }

    @Override
    public void setUserWebsiteURL(String userWebsiteURL) {
        app.setUserWebsiteURL(userWebsiteURL);
    }

    @Override
    public String getSignupEmailTemplate() {
        return app.getSignupEmailTemplate();
    }

    @Override
    public void setSignupEmailTemplate(String signupEmailTemplate) {
        app.setSignupEmailTemplate(signupEmailTemplate);
    }
}
