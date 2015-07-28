package com.dart.common.test.factory;

import com.dart.common.test.domain.DummyUser;
import com.dart.data.domain.User;
import com.dart.data.factory.UserFactory;

import java.util.Date;

/**
 * Dummy implementation of {@link UserFactory} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyUserFactory implements UserFactory {
    @Override
    public User createUser(String username, String displayName) {
        DummyUser instance = new DummyUser();
        instance.setId(username);
        instance.setDisplayName(displayName);
        instance.setDateCreated(new Date());
        return instance;
    }
}
