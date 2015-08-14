package com.dart.common.service.auth.facebook;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.properties.PropertiesProvider;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author RMPader
 */
public class FacebookTokenVerificationService implements TokenVerificationService {

    private HttpClient httpClient;
    private PropertiesProvider properties;
    private Gson gson = new Gson();
    private ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
        @Override
        public String handleResponse(
                final HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    };

    public FacebookTokenVerificationService(HttpClient httpClient, PropertiesProvider properties) {
        this.properties = properties;
        this.httpClient = httpClient;
    }

    @Override
    public boolean verifyToken(String token, String identity) {
        HttpGet getAppAccessToken = null;
        HttpGet getTokenInfo = null;
        try {
            getAppAccessToken = new HttpGet(properties.getFacebookEndpoint() + "/oauth/access_token?client_id=" + properties.getFacebookAppId() + "&client_secret=" + properties.getFacebookSecret() + "&grant_type=client_credentials");
            String responseBody = httpClient.execute(getAppAccessToken, responseHandler);
            String appAccessToken = URLEncoder.encode(responseBody.split("=")[1], "UTF-8");
            getTokenInfo = new HttpGet(properties.getFacebookEndpoint() + "/debug_token?input_token=" + token + "&access_token=" + appAccessToken);
            String receivedToken = httpClient.execute(getTokenInfo, responseHandler);
            FacebookToken fbToken = gson.fromJson(receivedToken, FacebookData.class).getData();
            return weAreTheAudienceOf(fbToken) && identity.equals(fbToken.getUserId()) && fbToken.getExpiration().after(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (getAppAccessToken != null) {
                getAppAccessToken.releaseConnection();
            }
            if (getTokenInfo != null) {
                getTokenInfo.releaseConnection();
            }
        }
    }

    private boolean weAreTheAudienceOf(FacebookToken fbToken) {
        return fbToken.getApplication().equals(properties.getAppName());
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

        public List<String> getScopes() {
            return scopes;
        }

        public String getUserId() {
            return user_id;
        }

        public boolean isValid() {
            return is_valid;
        }

        public void setIs_valid(Boolean is_valid) {
            this.is_valid = is_valid;
        }
    }
}
