package com.dart.data.objectify.repository;

import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.UserImpl;
import com.dart.data.repository.UserRepository;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

import static com.dart.data.objectify.ObjectifyProvider.objectify;

/**
 * Created by RMPader on 7/25/15.
 */
public class UserRepositoryImpl implements UserRepository {
    static {
        ObjectifyProvider.register(UserImpl.class);
    }

    @Override
    public User add(User entity) {
        if (objectify().load().entity(entity).now() == null) {
            Key<User> key = objectify().save().entity(entity).now();
            return getUserByKey(key);
        } else {
            throw new EntityCollisionException("The entity being added already exists");
        }
    }

    @Override
    public User retrieve(String id) {
        Key key = Key.create(UserImpl.class, id);
        return getUserByKey(key);
    }

    private User getUserByKey(Key<User> key) {
        User foundUser = objectify().load().key(key).safe();
        return foundUser;
    }

    @Override
    public User update(User entity) throws EntityNotFoundException {
        try {
            objectify().load().entity(entity).safe();
            Key<User> key = objectify().save().entity(entity).now();
            return getUserByKey(key);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tried to load an entity without a value in the field annotated with @Id.", e);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("No entity with an id matching the value in the field annotated with @Id.", e);
        }
    }

    @Override
    public void delete(User entity) {
        objectify().delete().entity(entity);
    }

    @Override
    public User retrieveByEmail(String email) {
        return retrieve(DigestUtils.sha256Hex(email));
    }
}
