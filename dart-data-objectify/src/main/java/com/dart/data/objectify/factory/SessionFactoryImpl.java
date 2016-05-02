package com.dart.data.objectify.factory;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.dart.data.factory.SessionFactory;
import com.dart.data.objectify.domain.SessionImpl;
import com.googlecode.objectify.Key;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author RMPader
 */
@Component
public class SessionFactoryImpl implements SessionFactory {
    @Override
    public Session createSession() {
        Session instance = new SessionImpl();
        return instance;
    }
}
