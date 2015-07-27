package com.dart.data.objectify.factory;

import com.dart.data.domain.User;
import com.dart.data.factory.UserFactory;
import com.dart.data.objectify.domain.UserImpl;

/**
 * Created by RMPader on 7/27/15.
 */
public class UserFactoryImpl implements UserFactory {

    @Override
    public User createUser(String username, String displayName) {
        return new UserImpl(username, displayName);
    }
}
