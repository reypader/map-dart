package com.dart.data.objectify.factory;

import com.dart.data.domain.Registration;
import com.dart.data.factory.RegistrationFactory;
import com.dart.data.objectify.domain.RegistrationImpl;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author RMPader
 */
@Component
public class RegistrationFactoryImpl implements RegistrationFactory {
    @Override
    public Registration createRegistration(String email, String displayName, String password) {
        String regCode = DigestUtils.shaHex(UUID.randomUUID().toString());
        Registration instance = new RegistrationImpl(regCode, email, displayName, password);
        return instance;
    }
}
