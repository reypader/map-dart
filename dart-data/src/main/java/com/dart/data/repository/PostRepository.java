package com.dart.data.repository;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;

import java.util.Collection;
import java.util.Date;

/**
 * Created by RMPader on 7/25/15.
 */
public interface PostRepository extends CrudRepository<Post> {

    Collection<Post> findPostsByUser(User user, int limit);

    Collection<Post> findPostsByUserSince(User user, Date date, int limit);

    Collection<Post> findPostsByEvent(Event event, int limit);

    Collection<Post> findPostsByEventSince(Event event, Date date, int limit);

}
