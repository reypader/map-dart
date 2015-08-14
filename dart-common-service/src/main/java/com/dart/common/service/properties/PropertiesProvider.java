package com.dart.common.service.properties;

/**
 * @author RMPader
 */
public interface PropertiesProvider {

    //GAE Properties
    String getGoogleAppId();
    String getGoogleAppSecret();

    //Login Properties
    String getGplusAppId();
    String getGplusAppSecret();
    String getGplusEndpoint();

    String getFacebookAppId();
    String getFacebookSecret();
    String getFacebookEndpoint();

    //Other stuff
    String getUserWebsiteURL();
    String getSignupEmailTemplate();
    String getAppName();
    int getDefaultTokenValidityDays();
}
