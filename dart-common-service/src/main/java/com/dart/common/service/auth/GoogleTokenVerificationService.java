package com.dart.common.service.auth;

import com.dart.common.service.properties.PropertiesProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

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
    public boolean verifyToken(String token, String identity) {
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null && idToken.getPayload().getEmail().equals(identity)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
