package com.dart.data.domain;

import java.util.Date;

/**
 * Session entity interface that provides necessary methods for performing business logic.
 *
 * @author RMPader
 */
public interface Session extends Entity {

    User getUser();

    Date getExpiry();

    String getDevice();

    String getBrowser();

    String getLocation();
}
