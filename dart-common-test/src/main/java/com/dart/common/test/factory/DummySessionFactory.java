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
    public Session createSession() {
        DummySession instance = new DummySession();
        instance.setDateCreated(new Date());
        return instance;
    }
}
