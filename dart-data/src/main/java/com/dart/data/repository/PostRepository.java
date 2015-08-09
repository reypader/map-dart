package com.dart.data.repository;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;

import java.util.Collection;
import java.util.Date;

/**
 * Interface for complex data access to stored {@link Post} entities.
 *
 * @author RMPader
 */
public interface PostRepository extends CrudRepository<Post> {

    /**
     * Retrieves the {@link Post} entities that have been created by the user before the given date.
     *
     * @param user  the user whose posts are being queried.
     * @param date  the date that marks the time when the posts should have already existed.
     * @param limit the maximum number of entities to return.
     * @return the collection of posts.
     */
    Collection<Post> findPostsByUserBefore(User user, Date date, int limit);

    /**
     * Retrieves the {@link Post} entities that have been created for the event before the given date.
     *
     * @param event the event whose posts are being queried.
     * @param date  the date that marks the time when the posts should have already existed.
     * @param limit the maximum number of entities to return.
     * @return the collection of posts.
     */
    Collection<Post> findPostsByEventBefore(Event event, Date date, int limit);

}
