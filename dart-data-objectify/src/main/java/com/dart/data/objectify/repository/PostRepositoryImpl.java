package com.dart.data.objectify.repository;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.PostImpl;
import com.dart.data.repository.PostRepository;
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
        if (((PostImpl) entity).getRawId() == null) {
            Key<Post> key = objectify().save().entity(entity).now();
            return getPostByKey(key);
        } else {
            throw new EntityCollisionException("The entity being added already has an ID. Did you mean to do an update?");
        }
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
    public Post update(Post entity) throws EntityNotFoundException {
        try {
            objectify().load().entity(entity).safe();
            Key<Post> key = objectify().save().entity(entity).now();
            return getPostByKey(key);
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
    public Collection<Post> findPostsByUserBefore(User user, Date date, int limit) {
        List<PostImpl> result = loadPost().filter("userRef", Ref.create(user)).filter("dateCreated >=", date).limit(limit).list();
        List<Post> posts = new ArrayList<>(result.size());
        posts.addAll(result);
        return posts;
    }

    @Override
    public Collection<Post> findPostsByEventBefore(Event event, Date date, int limit) {
        List<PostImpl> result = loadPost().filter("eventRef", Ref.create(event)).filter("dateCreated >=", date).limit(limit).list();
        List<Post> posts = new ArrayList<>(result.size());
        posts.addAll(result);
        return posts;
    }

    private LoadType<PostImpl> loadPost() {
        return objectify().load().type(PostImpl.class);
    }
}
