package com.dart.data.factory;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;

import java.net.InetAddress;
import java.util.Date;

/**
 * Factory that allows creation of Session instances in place of concrete constructors.
 *
 * @author RMPader
 */
public interface SessionFactory {

    Session createSession();
}
