package com.dart.data.repository;

import com.dart.data.domain.Registration;

/**
 * Interface for complex data access to stored {@link Registration} entities.
 *
 * @author RMPader
 */
public interface RegistrationRepository extends CrudRepository<Registration> {
    /**
     * Delete all registration for the particular email. This is usually invoked when multiple registrations have been
     * applied for and one of them verifies before the others.
     *
     * @param email the email address used during registration;
     */
    void deleteRegistrationForEmail(String email);
}
