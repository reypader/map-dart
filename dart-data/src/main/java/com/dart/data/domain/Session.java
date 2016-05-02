package com.dart.data.domain;

import org.springframework.session.ExpiringSession;

import java.util.Date;

/**
 * Session entity interface that provides necessary methods for performing business logic.
 *
 * @author RMPader
 */
public interface Session extends Entity, ExpiringSession {

    void setUser(User user);

    User getUser();

    String getIPAddress();

    String getDevice();

    String getBrowser();

    String getLocation();
}
