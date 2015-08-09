package com.dart.data.repository;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;

import java.util.Collection;

/**
 * Interface for complex data access to stored {@link Session} entities.
 *
 * @author RMPader
 */
public interface SessionRepository extends CrudRepository<Session> {

    /**
     * Find all associated sessions of the given user.
     *
     * @param user
     * @return the user's sessions.
     */
    Collection<Session> findSessionsOfUser(User user);

}
