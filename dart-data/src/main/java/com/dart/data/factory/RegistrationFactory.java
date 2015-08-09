package com.dart.data.factory;

import com.dart.data.domain.Registration;

/**
 * Factory that allows creation of Registration instances in place of concrete constructors.
 *
 * @author RMPader
 */
public interface RegistrationFactory {

    /**
     * Method for creating a registration using the required fields. The registration code of the instance will be
     * automatically generated and will serve as its ID.
     *
     * @param email       the intended email of the registering user.
     * @param displayName the intended display name of the registering user.
     * @param password    the intended password of the registering user.
     * @return the newly created {@link Registration} instance.
     */
    Registration createRegistration(String email, String displayName, String password);
}
