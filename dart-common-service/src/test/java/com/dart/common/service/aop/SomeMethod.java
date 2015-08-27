package com.dart.common.service.aop;

import com.dart.data.domain.User;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
@Singleton
public class SomeMethod implements MethodTest {

    @Authenticated
    @Override
    public User test(Object trash, User user, Object trash2, HttpServletRequest request) {
        return user;
    }
}
