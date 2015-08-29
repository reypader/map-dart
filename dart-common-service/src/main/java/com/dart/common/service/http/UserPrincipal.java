package com.dart.common.service.http;

import com.dart.data.domain.User;

import java.security.Principal;

/**
 * @author RMPader
 */
public class UserPrincipal implements Principal {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
