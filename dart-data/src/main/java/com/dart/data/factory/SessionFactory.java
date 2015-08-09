package com.dart.data.factory;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;

import java.util.Date;

/**
 * Factory that allows creation of Session instances in place of concrete constructors.
 *
 * @author RMPader
 */
public interface SessionFactory {

    /**
     * Method for creating a session with all the required fields provided.
     *
     * @param token    the token that will be used to uniquely identify this session.
     * @param user     the user associated with this session.
     * @param expiry   the date when this session should expire.
     * @param device   the name or description of the device associated with this session.
     * @param browser  the name or description of the browser associated with this session.
     * @param location the general place where the device was located when the session was created.
     * @return the newly created {@link Session} instance.
     */
    Session createSession(String token, User user, Date expiry, String device, String browser, String location);
}
