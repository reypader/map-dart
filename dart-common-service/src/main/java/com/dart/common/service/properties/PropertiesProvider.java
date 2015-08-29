package com.dart.common.service.properties;

/**
 * @author RMPader
 */
public interface PropertiesProvider {

    //GAE Properties
    String getGoogleAppId();
    String getGoogleServiceAccount();
    String getGoogleCloudStorageURL();
    String getGoogleCloudStorageBucket();


    //Login Properties
    String getGplusAppId();
    String getGplusAppSecret();
    String getGplusEndpoint();

    String getFacebookAppId();
    String getFacebookSecret();
    String getFacebookEndpoint();

    String getRecaptchaEndpoint();
    String getRecaptchaSecret();

    //Other stuff
    String getUserWebsiteURL();
    String getSignupEmailTemplate();
    String getAppName();
    int getDefaultTokenValidityDays();
}
