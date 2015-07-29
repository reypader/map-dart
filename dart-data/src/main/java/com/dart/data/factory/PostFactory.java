package com.dart.data.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;

/**
 * Factory that allows creation of Post classes
 * in place of concrete constructors.
 *
 * @author RMPader
 */
public interface PostFactory {

    /**
     * Method for creating a post with the minimum required fields.
     *
     * @param forEvent  the {@link Event} that the post should belong to.
     * @param byUser  the {@link User} that created the post.
     * @param content  the content of the post.
     * @return the newly created {@link Post} instance.
     */
    Post createPost(Event forEvent, User byUser, String content);

}
