package com.dart.data.objectify.factory;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.dart.data.factory.SessionFactory;
import com.dart.data.objectify.domain.SessionImpl;
import com.googlecode.objectify.Key;

import java.util.Date;

/**
 * @author RMPader
 */
public class SessionFactoryImpl implements SessionFactory {
    @Override
    public Session createSession(String token, User user, String ipAddress, Date expiry, String device, String browser, String location) {
        Session instance = new SessionImpl(token, Key.create(user), ipAddress, expiry, device, browser, location);
        return instance;
    }
}
