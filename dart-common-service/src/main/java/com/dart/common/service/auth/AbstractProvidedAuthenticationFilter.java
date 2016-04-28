package com.dart.common.service.auth;

import com.dart.data.domain.Identity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author RMPader
 */
public abstract class AbstractProvidedAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private TokenVerificationService verificationService;
    private String provider;

    public AbstractProvidedAuthenticationFilter(String loginUrl, String provider,
                                                TokenVerificationService verificationService) {
        super(new AntPathRequestMatcher(loginUrl, "POST"));
        this.verificationService = verificationService;
        this.provider = provider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            String providedIdentity = request.getParameter("identity");
            String token = request.getParameter("token");
            if (verificationService.verifyToken(token, providedIdentity)) {
                Identity identity = fetchIdentity(request, providedIdentity, provider);
                UserDetails details = new UserDetailsImpl(identity);
                return new PreAuthenticatedAuthenticationToken(details, providedIdentity, details.getAuthorities());
            } else {
                throw new BadCredentialsException("Identity mismatch for token");
            }
        } catch (BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Error while authenticating with provider (" + provider + ")", e);
        }
    }

    protected abstract Identity fetchIdentity(HttpServletRequest request, String providedIdentity, String provider);
}
