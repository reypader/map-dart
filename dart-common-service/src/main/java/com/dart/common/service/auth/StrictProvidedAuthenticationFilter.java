package com.dart.common.service.auth;

import com.dart.data.domain.Identity;
import com.dart.data.repository.IdentityRepository;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rpader on 4/28/16.
 */
public class StrictProvidedAuthenticationFilter extends AbstractProvidedAuthenticationFilter {

    private IdentityRepository identityRepository;

    public StrictProvidedAuthenticationFilter(String loginUrl, String provider,
                                              TokenVerificationService verificationService,
                                              IdentityRepository identityRepository) {
        super(loginUrl, provider, verificationService);
        this.identityRepository = identityRepository;
    }

    @Override
    protected Identity fetchIdentity(HttpServletRequest request, String providedIdentity, String provider) {
        Identity identity = identityRepository.findIdentityFromProvider(providedIdentity, provider);
        if (identity == null) {
            throw new BadCredentialsException("Unknown user");
        } else {
            return identity;
        }
    }
}
