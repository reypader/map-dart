package com.dart.common.service.auth.google;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.properties.PropertiesProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author RMPader
 */
public class GoogleTokenVerificationService implements TokenVerificationService {

    private GoogleIdTokenVerifier verifier;
    private PropertiesProvider propertiesProvider;

    public GoogleTokenVerificationService(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(propertiesProvider.getGplusAppId()))
                .build();
    }

    @Override
    public boolean verifyToken(String token, String identity) throws Exception {
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null && idToken.getPayload()
                                      .getEmail()
                                      .equals(identity)) {
            return true;
        }
        return false;
    }
}
