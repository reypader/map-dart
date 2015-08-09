package com.dart.data.objectify.factory;

import com.dart.data.domain.Identity;
import com.dart.data.domain.User;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.objectify.domain.IdentityImpl;
import com.googlecode.objectify.Key;

/**
 * @author RMPader
 */
public class IdentityFactoryImpl implements IdentityFactory {
    @Override
    public Identity createIdentity(User user, String provider, String providedIdentity) {
        Identity instance = new IdentityImpl(Key.create(user), provider, providedIdentity);
        return instance;
    }
}
