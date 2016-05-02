package com.dart.data.objectify.factory;

import com.dart.data.domain.User;
import com.dart.data.factory.UserFactory;
import com.dart.data.objectify.domain.UserImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by RMPader on 7/27/15.
 */
@Component
public class UserFactoryImpl implements UserFactory {

    @Override
    public User createUser(String email, String displayName) {
        String secret = DigestUtils.sha256Hex(UUID.randomUUID().toString());
        User instance = new UserImpl(email, displayName);
        instance.setSecret(secret);
        return instance;
    }
}
