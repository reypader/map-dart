package com.dart.data.factory;

import com.dart.data.domain.Identity;
import com.dart.data.domain.User;

/**
 * Factory that allows creation of Identity instances in place of concrete constructors.
 *
 * @author RMPader
 */
public interface IdentityFactory {

    /**
     * * Method for creating an identity with all the required fields provided.
     *
     * @param user             the user to whom this identity will be associated with.
     * @param provider         the organization that provided this identity.
     * @param providedIdentity the user's identity according to the provider.
     * @return the newly created {@link Identity} instance.
     */
    Identity createIdentity(User user, String provider, String providedIdentity);
}
