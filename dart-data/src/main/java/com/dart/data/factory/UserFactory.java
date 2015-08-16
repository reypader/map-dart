package com.dart.data.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;

/**
 * Factory that allows creation of User instances
 * in place of concrete constructors.
 *
 * @author RMPader
 */
public interface UserFactory {

    /**
     * Method for creating a user with the minimum required fields.
     *
     * @param email  the unique email of this user.
     * @param displayName  the name of the user that will be displayed in the UI.
     * @return the newly created {@link User} instance.
     */
    User createUser(String email, String displayName);

}
