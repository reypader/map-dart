package com.dart.data.factory;

import com.dart.data.domain.User;

/**
 * Created by RMPader on 7/27/15.
 */
public interface UserFactory {

    User createUser(String username, String displayName);

}
