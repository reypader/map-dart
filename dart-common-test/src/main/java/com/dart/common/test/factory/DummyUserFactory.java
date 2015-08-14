package com.dart.common.test.factory;

import com.dart.common.test.domain.DummyUser;
import com.dart.data.domain.User;
import com.dart.data.factory.UserFactory;

import java.util.Date;
import java.util.UUID;

/**
 * Dummy implementation of {@link UserFactory} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyUserFactory implements UserFactory {
    @Override
    public User createUser(String email, String displayName) {
        DummyUser instance = new DummyUser();
        instance.setId(UUID.randomUUID().toString());
        instance.setEmail(email);
        instance.setDisplayName(displayName);
        instance.setDateCreated(new Date());
        instance.setSecret(UUID.randomUUID().toString());
        return instance;
    }
}
