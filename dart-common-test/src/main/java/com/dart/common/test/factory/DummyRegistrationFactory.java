package com.dart.common.test.factory;

import com.dart.common.test.domain.DummyRegistration;
import com.dart.data.domain.Registration;
import com.dart.data.factory.RegistrationFactory;

import java.util.Date;
import java.util.UUID;

/**
 * @author RMPader
 */
public class DummyRegistrationFactory implements RegistrationFactory {
    @Override
    public Registration createRegistration(String email, String displayName, String password) {
        DummyRegistration instance = new DummyRegistration();
        instance.setId(UUID.randomUUID().toString());
        instance.setDateCreated(new Date());
        instance.setDisplayName(displayName);
        instance.setEmail(email);
        instance.setPassword(password);
        return instance;
    }
}
