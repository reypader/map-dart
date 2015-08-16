package com.dart.data.repository;

import com.dart.data.domain.User;

/**
 * Interface for complex data access to stored {@link User} entities.
 *
 * @author RMPader
 */
public interface UserRepository extends CrudRepository<User> {
    User retrieveByEmail(String email);
}
