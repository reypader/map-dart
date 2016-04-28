package com.dart.common.service.auth.google;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.properties.PropertiesProvider;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RMPader
 */
public class RecaptchaTokenVerificationService implements TokenVerificationService {

    private HttpClient httpClient;
    private PropertiesProvider properties;
    private Gson gson = new Gson();
    private ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
        @Override
        public String handleResponse(
                final HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine()
                                 .getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    };

    public RecaptchaTokenVerificationService(HttpClient httpClient, PropertiesProvider properties) {
        this.properties = properties;
        this.httpClient = httpClient;
    }

    @Override
    public boolean verifyToken(String token, String identity) {
        HttpPost validateRecaptcha = null;
        try {
            validateRecaptcha = new HttpPost(properties.getRecaptchaEndpoint());
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("secret", properties.getRecaptchaSecret()));
            nvps.add(new BasicNameValuePair("response", token));
            nvps.add(new BasicNameValuePair("remoteip", identity));
            validateRecaptcha.setEntity(new UrlEncodedFormEntity(nvps));
            String responseBody = httpClient.execute(validateRecaptcha, responseHandler);
            RecaptchaResult result = gson.fromJson(responseBody, RecaptchaResult.class);
            return result.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (validateRecaptcha != null) {
                validateRecaptcha.releaseConnection();
            }
        }
    }

    public static class RecaptchaResult {

        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
