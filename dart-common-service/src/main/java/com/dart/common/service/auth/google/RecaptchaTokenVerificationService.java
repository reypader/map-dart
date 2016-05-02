package com.dart.common.service.auth.google;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.http.WebClient;
import com.dart.common.service.http.exception.WebClientException;
import com.dart.common.service.properties.PropertiesProvider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.google.common.net.MediaType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author RMPader
 */
@Service
@Recaptcha
public class RecaptchaTokenVerificationService implements TokenVerificationService {
    private static final Logger logger = Logger.getLogger(RecaptchaTokenVerificationService.class.getName());

    private WebClient webClient;
    private ObjectMapper mapper = new ObjectMapper();
    private PropertiesProvider properties;

    @Autowired
    public RecaptchaTokenVerificationService(WebClient webClient, PropertiesProvider properties) {
        this.properties = properties;
        this.webClient = webClient;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public boolean verifyToken(String token, String identity) throws IOException, WebClientException {
        HttpPost validateRecaptcha = null;
        try {
            String data = "secret="+properties.getRecaptchaSecret();
            data += "&response="+token;
            data += "&remoteip="+identity;

            InputStream response = webClient.post(properties.getRecaptchaEndpoint(), MediaType.FORM_DATA, data.getBytes(),
                                                Collections.EMPTY_MAP);
            RecaptchaResult result = mapper.readValue(response, RecaptchaResult.class);
            return result.isSuccess();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while connecting to Recaptcha server: ", e);
            throw e;
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
