package com.dart.common.service.auth;

import com.dart.data.domain.Identity;
import com.dart.data.repository.IdentityRepository;
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
public class ProvidedAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private IdentityRepository identityRepository;
    private TokenVerificationService verificationService;
    private String provider;

    public ProvidedAuthenticationFilter(String loginUrl, String provider,
                                        IdentityRepository identityRepository,
                                        TokenVerificationService verificationService) {
        super(new AntPathRequestMatcher(loginUrl, "POST"));
        this.identityRepository = identityRepository;
        this.verificationService = verificationService;
        this.provider = provider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            String credentials = request.getParameter("identity");
            String token = request.getParameter("token");
            if (verificationService.verifyToken(token, credentials)) {
                Identity identity = identityRepository.findIdentityFromProvider(credentials, provider);
                UserDetails details = new UserDetailsImpl(identity);
                return new PreAuthenticatedAuthenticationToken(details, credentials, details.getAuthorities());
            } else {
                throw new BadCredentialsException("Identity mismatch for token");
            }
        } catch (BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Error while authenticating with provider (" + provider + ")", e);
        }
    }
}
