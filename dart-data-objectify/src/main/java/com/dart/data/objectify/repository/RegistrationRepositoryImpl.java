package com.dart.data.objectify.repository;

import com.dart.data.domain.Entity;
import com.dart.data.domain.Registration;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.RegistrationImpl;
import com.dart.data.repository.RegistrationRepository;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

import static com.dart.data.objectify.ObjectifyProvider.objectify;

/**
 * Created by RMPader on 7/25/15.
 */
public class RegistrationRepositoryImpl implements RegistrationRepository {
    static {
        ObjectifyProvider.register(RegistrationImpl.class);
    }

    @Override
    public Registration add(Registration entity) {
        Key<Registration> key = objectify().save().entity(entity).now();
        return getRegistrationByKey(key);
    }

    @Override
    public Registration retrieve(String id) {
        Key key = Key.create(RegistrationImpl.class, id);
        return getRegistrationByKey(key);
    }

    private Registration getRegistrationByKey(Key<Registration> key) {
        Registration foundRegistration = objectify().load().key(key).safe();
        return foundRegistration;
    }

    @Override
    public Registration update(Registration entity) {
        ensureExistingRegistration(entity);
        return add(entity);
    }

    private void ensureExistingRegistration(Entity post) {
        try {
            objectify().load().entity(post).safe();
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tried to load an entity without a value in the field annotated with @Id.", e);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("No entity with an id matching the value in the field annotated with @Id.", e);
        }
    }

    @Override
    public void delete(Registration entity) {
        objectify().delete().entity(entity);
    }

}
