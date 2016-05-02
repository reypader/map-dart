package com.dart.common.service.auth.google;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.http.WebClient;
import com.dart.common.service.http.exception.WebClientException;
import com.dart.common.service.property.PropertiesProvider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
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
    private PropertiesProvider.ThirdPartyApi recaptcha;

    @Autowired
    public RecaptchaTokenVerificationService(WebClient webClient, PropertiesProvider properties) {
        this.recaptcha = properties.getRecaptcha();
        this.webClient = webClient;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public boolean verifyToken(String token, String identity) throws IOException, WebClientException {
        try {
            String data = "secret=" + recaptcha.getSecret();
            data += "&response=" + token;
            data += "&remoteip=" + identity;

            InputStream response = webClient.post(recaptcha.getEndpoint(), MediaType.FORM_DATA, data.getBytes(),
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
