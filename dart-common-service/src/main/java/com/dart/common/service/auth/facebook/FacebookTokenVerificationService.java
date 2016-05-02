package com.dart.common.service.auth.facebook;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.http.WebClient;
import com.dart.common.service.property.PropertiesProvider;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author RMPader
 */
@Service
@Facebook
public class FacebookTokenVerificationService implements TokenVerificationService {

    private static final Logger logger = Logger.getLogger(FacebookTokenVerificationService.class.getName());
    private WebClient webClient;
    private PropertiesProvider.ThirdPartyApi facebook;
    private String appName;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public FacebookTokenVerificationService(WebClient webClient, PropertiesProvider properties) {
        this.facebook = properties.getFacebook();
        this.appName = properties.getName();
        this.webClient = webClient;
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public boolean verifyToken(String token, String identity) throws Exception {
        try {
            InputStream tokenResponse = webClient.get(
                    facebook.getEndpoint() + "/oauth/access_token?client_id=" + facebook.getAppId() + "&client_secret=" + facebook.getSecret() + "&grant_type=client_credentials",
                    Collections.<String, Object>emptyMap());
            String tokenResponseBody = IOUtils.toString(tokenResponse, "UTF-8");
            String appAccessToken = URLEncoder.encode(tokenResponseBody.split("=")[1],
                                                      "UTF-8");

            InputStream infoResponse = webClient.get(
                    facebook.getEndpoint() + "/debug_token?input_token=" + token + "&access_token=" + appAccessToken,
                    Collections.<String, Object>emptyMap());
            FacebookToken fbToken = mapper.readValue(infoResponse, FacebookData.class).getData();
            return weAreTheAudienceOf(fbToken) && identity.equals(fbToken.getUserId()) && fbToken.getExpiration()
                                                                                                 .after(new Date()) && fbToken.isValid();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while connecting to Facebook server: ", e);
            throw e;
        }
    }

    private boolean weAreTheAudienceOf(FacebookToken fbToken) {
        return fbToken.getApplication()
                      .equals(appName);
    }

    /**
     * @author RMPader
     */
    public static class FacebookData {

        private FacebookToken data;

        public FacebookToken getData() {
            return data;
        }
    }

    public static class FacebookToken {

        private String app_id;
        private String application;
        private Long expires_at;
        private Boolean is_valid;
        private ArrayList<String> scopes;
        private String user_id;

        public String getAppId() {
            return app_id;
        }

        public String getApplication() {
            return application;
        }

        public Date getExpiration() {
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(expires_at * 1000);
            return now.getTime();
        }

        public Long getExpires_at() {
            return expires_at;
        }

        public void setExpires_at(Long expires_at) {
            this.expires_at = expires_at;
        }

        public List<String> getScopes() {
            return scopes;
        }

        public String getUserId() {
            return user_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public boolean isValid() {
            return is_valid;
        }

        public void setIs_valid(Boolean is_valid) {
            this.is_valid = is_valid;
        }
    }
}
