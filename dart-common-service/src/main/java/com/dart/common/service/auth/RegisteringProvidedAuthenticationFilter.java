package com.dart.common.service.auth;

import com.dart.data.domain.Identity;
import com.dart.data.domain.User;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.factory.UserFactory;
import com.dart.data.repository.IdentityRepository;
import com.dart.data.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rpader on 4/28/16.
 */
public class RegisteringProvidedAuthenticationFilter extends AbstractProvidedAuthenticationFilter {

    private IdentityRepository identityRepository;
    private UserRepository userRepository;
    private UserFactory userFactory;
    private IdentityFactory identityFactory;

    public RegisteringProvidedAuthenticationFilter(String loginUrl, String provider,
                                                   TokenVerificationService verificationService,
                                                   IdentityRepository identityRepository, UserRepository userRepository,
                                                   UserFactory userFactory, IdentityFactory identityFactory) {
        super(loginUrl, provider, verificationService);
        this.identityRepository = identityRepository;
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.identityFactory = identityFactory;
    }

    @Override
    protected Identity fetchIdentity(HttpServletRequest request, String providedIdentity, String provider) {
        Identity identity = identityRepository.findIdentityFromProvider(providedIdentity, provider);
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        if (identity == null) {
            User user = userRepository.retrieveByEmail(email);
            if (user == null) {
                user = userFactory.createUser(email, name);
                user = userRepository.add(user);
                user.setPhotoURL(request.getParameter("photo"));
            }
            identity = identityFactory.createIdentity(user, provider, providedIdentity);
            identityRepository.add(identity);
        }
        return identity;
    }
}
