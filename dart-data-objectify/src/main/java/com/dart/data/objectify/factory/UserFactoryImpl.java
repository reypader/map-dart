package com.dart.data.objectify.factory;

import com.dart.data.domain.User;
import com.dart.data.factory.UserFactory;
import com.dart.data.objectify.domain.UserImpl;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * Created by RMPader on 7/27/15.
 */
public class UserFactoryImpl implements UserFactory {

    @Override
    public User createUser(String email, String displayName) {
        String secret = DigestUtils.shaHex(UUID.randomUUID().toString());
        User instance = new UserImpl(email, displayName);
        instance.setSecret(secret);
        return instance;
    }
}
