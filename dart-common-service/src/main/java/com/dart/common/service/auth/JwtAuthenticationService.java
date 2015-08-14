package com.dart.common.service.auth;

import com.dart.common.service.properties.PropertiesProvider;
import com.dart.common.service.util.IPAddressHelper;
import com.dart.data.domain.User;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.oauth.jsontoken.Checker;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;
import org.joda.time.Instant;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author RMPader
 */
public class JwtAuthenticationService implements AuthenticationService {

    private final PropertiesProvider propertiesProvider;
    private final Checker checker;

    public JwtAuthenticationService(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
        this.checker = new CheckerImpl(propertiesProvider);
    }

    /**
     * Generate a JWT for the given user based on the IP address used for the request. The created JWT will expire at
     * the date indicated. The signature will be signed using the user's secret.
     *
     * @param expiry  the date of expiry for the token.
     * @param user    the user to whom this token is associated with.
     * @param request the HTTP request used to request for the token.
     * @return the JWT signed with the user's secret.
     */
    @Override
    public String generateToken(Date expiry, User user, HttpServletRequest request) {
        try {
            HmacSHA256Signer signer = new HmacSHA256Signer(propertiesProvider.getAppName(), null, user.getSecret().getBytes());
            Calendar now = Calendar.getInstance();

            JsonToken token = new JsonToken(signer);
            token.setAudience(propertiesProvider.getAppName());
            token.setIssuedAt(new Instant(now.getTimeInMillis()));
            token.setExpiration(new Instant(expiry.getTime()));

            JsonObject payload = token.getPayloadAsJsonObject();
            payload.addProperty("user", user.getId());
            payload.addProperty("client", BCrypt.hashpw(IPAddressHelper.getIPAddress(request), BCrypt.gensalt()));
            String tokenId = token.serializeAndSign();

            return tokenId;
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verify if the token included in the request is signed with the user's secret key and that the originating IP
     * address matches the one indicated in the token.
     *
     * @param user
     * @param request
     * @return
     */
    @Override
    public boolean verifyToken(User user, HttpServletRequest request) {
        try {
            String[] authHeader = request.getHeader("Authorization").split(" ");
            String authHeaderType = authHeader[0];
            String authHeaderValue = authHeader[1];
            if (authHeaderType.equals("Bearer")) {
                Verifier hmacVerifier = new HmacSHA256Verifier(user.getSecret().getBytes());
                VerifierProvider hmacLocator = new VerifierProviderImpl(hmacVerifier);
                VerifierProviders locators = new VerifierProviders();
                locators.setVerifierProvider(SignatureAlgorithm.HS256, hmacLocator);
                JsonTokenParser parser = new JsonTokenParser(locators, checker);
                JsonToken jt = parser.verifyAndDeserialize(authHeaderValue);
                JsonObject payload = jt.getPayloadAsJsonObject();
                String claimId = payload.getAsJsonPrimitive("user").getAsString();
                String claimIp = payload.getAsJsonPrimitive("client").getAsString();
                return BCrypt.checkpw(IPAddressHelper.getIPAddress(request), claimIp) && user.getId().equals(claimId);
            } else {
                return false;
            }
        } catch (SignatureException e) {
            return false;
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static class VerifierProviderImpl implements VerifierProvider {

        private final Verifier verifier;

        public VerifierProviderImpl(Verifier verifier) {
            this.verifier = verifier;
        }

        @Override
        public List<Verifier> findVerifier(String id, String key) {
            return Lists.newArrayList(verifier);
        }
    }

    public static class CheckerImpl implements Checker {

        private PropertiesProvider propertiesProvider;

        public CheckerImpl(PropertiesProvider propertiesProvider) {
            this.propertiesProvider = propertiesProvider;
        }

        @Override
        public void check(JsonObject payload) throws SignatureException {
            String audience = payload.getAsJsonPrimitive("aud").getAsString();
            if (!audience.equals(propertiesProvider.getAppName())) {
                throw new SignatureException("Audience does not match");
            }
        }

    }

}
