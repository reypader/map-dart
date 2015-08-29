package com.dart.common.service.aop;

import com.dart.common.service.http.UserPrincipal;
import com.dart.data.domain.User;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
@Singleton
public class SomeMethod {

    @Authenticated
    public User test(Object trash2, HttpServletRequest request) {
        return ((UserPrincipal) request.getUserPrincipal()).getUser();
    }
}
