package com.dart.common.service.auth;

import com.dart.common.service.properties.PropertiesProvider;
import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.dart.data.factory.SessionFactory;
import com.dart.data.repository.SessionRepository;
import com.google.gson.JsonObject;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import org.joda.time.Instant;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author RMPader
 */
public class JwtAuthenticationTokenService implements AuthenticationTokenService {
    private final SessionFactory sessionFactory;
    private final SessionRepository sessionRepository;
    private final PropertiesProvider propertiesProvider;

    public JwtAuthenticationTokenService(SessionFactory sessionFactory, SessionRepository sessionRepository, PropertiesProvider propertiesProvider) {
        this.sessionFactory = sessionFactory;
        this.sessionRepository = sessionRepository;
        this.propertiesProvider = propertiesProvider;
    }

    @Override
    public String generateSession(Date expiry, User user, HttpServletRequest request) {
        try {
            HmacSHA256Signer signer = new HmacSHA256Signer(propertiesProvider.getAppName(), null, propertiesProvider.getOtherSecret().getBytes());
            Calendar cal = Calendar.getInstance();

            JsonToken token = new JsonToken(signer);
            token.setAudience(propertiesProvider.getAppName());
            token.setIssuedAt(new Instant(cal.getTimeInMillis()));
            token.setExpiration(new Instant(expiry.getTime()));

            JsonObject payload = token.getPayloadAsJsonObject();
            payload.addProperty("user", user.getId());
            String tokenId = token.serializeAndSign();

            Session session = sessionFactory.createSession(x`)

            return tokenId;
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifySession(User user, HttpServletRequest request) {
        return true;
    }

    @Override
    public void invalidateSession(String sessionId) {

    }
}
