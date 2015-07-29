package com.dart.common.test.repository;

import com.dart.common.test.domain.DummyPost;
import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;
import com.dart.data.repository.PostRepository;

import java.util.*;

/**
 * Dummy implementation of {@link PostRepository} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyPostRepository implements PostRepository {

    private Map<String, Post> dummyStore = new HashMap<>();

    public Map<String, Post> getStoredData() {
        return dummyStore;
    }

    @Override
    public Collection<Post> findPostsByUserBefore(User organizer, Date date, int limit) {
        List<Post> posts = new ArrayList<>();
        for (Post post : dummyStore.values()) {
            if (post.getDateCreated().before(date) && post.getUser().getId().equals(organizer.getId())) {
                posts.add(post);
            }
            if (posts.size() >= limit) {
                return posts;
            }
        }
        return posts;
    }

    @Override
    public Collection<Post> findPostsByEventBefore(Event event, Date date, int limit) {
        List<Post> posts = new ArrayList<>();
        for (Post post : dummyStore.values()) {
            if (post.getDateCreated().before(date) && post.getEvent().getId().equals(event.getId())) {
                posts.add(post);
            }
            if (posts.size() >= limit) {
                return posts;
            }
        }
        return posts;
    }

    @Override
    public Post add(Post entity) {
        DummyPost post = (DummyPost) entity;
        String id = UUID.randomUUID().toString();
        post.setId(id);
        dummyStore.put(id, post);
        return post;
    }

    @Override
    public Post retrieve(String id) {
        return dummyStore.get(id);
    }

    @Override
    public Post update(Post entity) {
        dummyStore.remove(entity.getId());
        dummyStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Post entity) {
        dummyStore.remove(entity.getId());
    }
}
