package com.dart.common.service.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * {@link PropertiesProvider} that reads from an input stream to populate property values.
 *
 * @author RMPader
 */
public class FilePropertiesProvider implements PropertiesProvider {

    private String googleAppId;
    private String googleAppSecret;
    private String gplusAppId;
    private String gplusAppSecret;
    private String gplusLoginEndpoint;
    private String facebookAppId;
    private String facebookLoginEndpoint;
    private String facebookAppSecret;
    private String signupEmailTemplate;
    private String userWebsite;
    private String appName;
    private String recaptchaEndpoint;
    private String recaptchaSecret;
    private int defaultTokenValidityDays;

    public FilePropertiesProvider(InputStream stream) {
        Properties prop = new Properties();
        try {
            prop.load(stream);
            this.googleAppId = prop.getProperty("google.app.id");
            this.googleAppSecret = prop.getProperty("google.app.secret");
            this.gplusAppId = prop.getProperty("gplus.app.id");
            this.gplusAppSecret = prop.getProperty("gplus.app.secret");
            this.gplusLoginEndpoint = prop.getProperty("gplus.endpoint");
            if (gplusLoginEndpoint.endsWith("/")) {
                this.gplusLoginEndpoint = gplusLoginEndpoint.substring(0, gplusLoginEndpoint.length() - 1);
            }
            this.facebookAppId = prop.getProperty("facebook.app.id");
            this.facebookAppSecret = prop.getProperty("facebook.app.secret");
            this.facebookLoginEndpoint = prop.getProperty("facebook.endpoint");
            if (facebookLoginEndpoint.endsWith("/")) {
                this.facebookLoginEndpoint = facebookLoginEndpoint.substring(0, facebookLoginEndpoint.length() - 1);
            }
            this.defaultTokenValidityDays = Integer.valueOf(prop.getProperty("sys.token.validity.days"));
            this.signupEmailTemplate = prop.getProperty("sys.signup.email");
            this.userWebsite = prop.getProperty("sys.ui.web");
            if (userWebsite.endsWith("/")) {
                this.userWebsite = userWebsite.substring(0, userWebsite.length() - 1);
            }
            this.appName = prop.getProperty("sys.name");
            this.recaptchaEndpoint = prop.getProperty("recaptcha.endpoint");
            this.recaptchaSecret = prop.getProperty("recaptcha.secret");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getGoogleAppId() {
        return googleAppId;
    }

    @Override
    public String getGoogleAppSecret() {
        return googleAppSecret;
    }

    @Override
    public String getGplusAppId() {
        return gplusAppId;
    }

    @Override
    public String getGplusAppSecret() {
        return gplusAppSecret;
    }

    @Override
    public String getGplusEndpoint() {
        return gplusLoginEndpoint;
    }

    @Override
    public String getFacebookAppId() {
        return facebookAppId;
    }

    @Override
    public String getFacebookSecret() {
        return facebookAppSecret;
    }

    @Override
    public String getFacebookEndpoint() {
        return facebookLoginEndpoint;
    }

    @Override
    public String getRecaptchaEndpoint() {
        return recaptchaEndpoint;
    }

    @Override
    public String getRecaptchaSecret() {
        return recaptchaSecret;
    }

    @Override
    public String getUserWebsiteURL() {
        return userWebsite;
    }

    @Override
    public String getSignupEmailTemplate() {
        return signupEmailTemplate;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public int getDefaultTokenValidityDays() {
        return defaultTokenValidityDays;
    }
}
