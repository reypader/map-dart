package com.dart.common.test.factory;

import com.dart.common.test.domain.DummyIdentity;
import com.dart.data.domain.Identity;
import com.dart.data.domain.User;
import com.dart.data.factory.IdentityFactory;

import java.util.Date;
import java.util.UUID;

/**
 * @author RMPader
 */
public class DummyIdentityFactory implements IdentityFactory {
    @Override
    public Identity createIdentity(User user, String provider, String providedIdentity) {
        DummyIdentity instance = new DummyIdentity();
        instance.setUser(user);
        instance.setDateCreated(new Date());
        instance.setProvidedIdentity(providedIdentity);
        instance.setProvider(provider);
        instance.setId(UUID.randomUUID().toString());
        return instance;
    }
}
