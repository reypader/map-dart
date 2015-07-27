package com.dart.data.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;

/**
 * Created by RMPader on 7/27/15.
 */
public interface PostFactory {

    Post createPost(Event forEvent, User byUser, String content);

}
