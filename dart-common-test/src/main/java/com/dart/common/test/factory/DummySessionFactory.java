package com.dart.common.test.factory;

import com.dart.common.test.domain.DummySession;
import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.dart.data.factory.SessionFactory;

import java.util.Date;

/**
 * @author RMPader
 */
public class DummySessionFactory implements SessionFactory {
    @Override
    public Session createSession(String token, User user, String ipAddress, Date expiry, String device, String browser, String location) {
        DummySession instance = new DummySession();
        instance.setId(token);
        instance.setDateCreated(new Date());
        instance.setBrowser(browser);
        instance.setDevice(device);
        instance.setExpiry(expiry);
        instance.setLocation(location);
        instance.setUser(user);
        instance.setIPAddress(ipAddress);
        return instance;
    }
}
