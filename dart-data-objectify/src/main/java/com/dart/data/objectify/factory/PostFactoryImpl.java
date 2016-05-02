package com.dart.data.objectify.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;
import com.dart.data.factory.PostFactory;
import com.dart.data.objectify.domain.PostImpl;
import com.googlecode.objectify.Key;
import org.springframework.stereotype.Component;

/**
 * Created by RMPader on 7/27/15.
 */
@Component
public class PostFactoryImpl implements PostFactory {

    @Override
    public Post createPost(Event forEvent, User byUser, String content) {
        return new PostImpl(Key.create(forEvent), Key.create(byUser), content);
    }
}
