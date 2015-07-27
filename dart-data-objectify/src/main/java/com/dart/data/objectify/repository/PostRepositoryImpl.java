package com.dart.data.objectify.repository;

import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.domain.Event;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.PostImpl;
import com.dart.data.domain.Post;
import com.dart.data.repository.PostRepository;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.LoadType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.dart.data.objectify.ObjectifyProvider.objectify;

/**
 * Created by RMPader on 7/25/15.
 */
public class PostRepositoryImpl implements PostRepository {

    static {
        ObjectifyProvider.register(PostImpl.class);
    }

    @Override
    public Post add(Post entity) {
        Key<Post> key = objectify().save().entity(entity).now();
        return getPostByKey(key);
    }

    @Override
    public Post retrieve(String id) {
        Key<Post> key = Key.create(id);
        return getPostByKey(key);
    }

    private Post getPostByKey(Key<Post> key) {
        Post foundPost = objectify().load().key(key).safe();
        return foundPost;
    }

    @Override
    public Post update(Post entity) {
        ensureExistingPost(entity);
        return add(entity);
    }

    private void ensureExistingPost(Post post) {
        try {
            objectify().load().entity(post).safe();
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tried to load an entity without a value in the field annotated with @Id.", e);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("No entity with an id matching the value in the field annotated with @Id.", e);
        }
    }

    @Override
    public void delete(Post entity) {
        objectify().delete().entity(entity);
    }

    @Override
    public Collection<Post> findPostsByUser(User user, int limit) {
        List<PostImpl> result = loadPost().filter("userRef", Ref.create(user)).limit(limit).list();
        List<Post> posts = new ArrayList<>();
        posts.addAll(result);
        return posts;
    }

    @Override
    public Collection<Post> findPostsByUserSince(User user, Date date, int limit) {
        List<PostImpl> result = loadPost().filter("userRef", Ref.create(user)).filter("dateCreated >=", date).limit(limit).list();
        List<Post> posts = new ArrayList<>();
        posts.addAll(result);
        return posts;
    }

    @Override
    public Collection<Post> findPostsByEvent(Event event, int limit) {
        List<PostImpl> result = loadPost().filter("eventRef", Ref.create(event)).limit(limit).list();
        List<Post> posts = new ArrayList<>();
        posts.addAll(result);
        return posts;
    }

    @Override
    public Collection<Post> findPostsByEventSince(Event event, Date date, int limit) {
        List<PostImpl> result = loadPost().filter("eventRef", Ref.create(event)).filter("dateCreated >=", date).limit(limit).list();
        List<Post> posts = new ArrayList<>();
        posts.addAll(result);
        return posts;
    }

    private LoadType<PostImpl> loadPost() {
        return objectify().load().type(PostImpl.class);
    }
}
