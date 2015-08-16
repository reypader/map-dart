package com.dart.data.objectify.repository;

import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.UserImpl;
import com.dart.data.repository.UserRepository;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dart.data.objectify.ObjectifyProvider.objectify;

/**
 * Created by RMPader on 7/25/15.
 */
public class UserRepositoryImpl implements UserRepository {
    Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());

    static {
        ObjectifyProvider.register(UserImpl.class);
    }

    @Override
    public User add(User entity) {
        int emailUsage = objectify().load().type(UserImpl.class).filter("email", entity.getEmail()).list().size();
        if (((UserImpl) entity).getRawId() == null && emailUsage == 0) {
            Key<User> key = objectify().save().entity(entity).now();
            return getUserByKey(key);
        } else {
            throw new EntityCollisionException("The entity being added already exists");
        }
    }

    @Override
    public User retrieve(String id) {
        Key<User> key = Key.create(id);
        return (UserImpl) objectify().load().key(key).now();
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
        List<UserImpl> users = objectify().load().type(UserImpl.class).filter("email", email).list();
        if (users.size() < 1) {
            return null;
        } else if (users.size() == 1) {
            return users.get(0);
        } else {
            logger.log(Level.SEVERE, "Duplicate email address usage detected");
            throw new IllegalStateException("Duplicate email address usage detected");
        }
    }
}
