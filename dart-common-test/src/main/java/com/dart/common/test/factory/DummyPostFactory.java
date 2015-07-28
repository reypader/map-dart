package com.dart.common.test.factory;

import com.dart.common.test.domain.DummyPost;
import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;
import com.dart.data.factory.PostFactory;

import java.util.Date;

/**
 * Dummy implementation of {@link PostFactory} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyPostFactory implements PostFactory {
    @Override
    public Post createPost(Event forEvent, User byUser, String content) {
        DummyPost instance = new DummyPost();
        instance.setDateCreated(new Date());
        instance.setEvent(forEvent);
        instance.setUser(byUser);
        instance.setContent(content);
        return instance;
    }
}
