package com.dart.data.objectify.repository;

import com.dart.data.domain.Identity;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.IdentityImpl;
import com.dart.data.repository.IdentityRepository;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import org.springframework.stereotype.Component;

import static com.dart.data.objectify.ObjectifyProvider.objectify;

/**
 * Created by RMPader on 7/25/15.
 */
@Component
public class IdentityRepositoryImpl implements IdentityRepository {
    static {
        ObjectifyProvider.register(IdentityImpl.class);
    }

    @Override
    public Identity add(Identity entity) {
        if (objectify().load().entity(entity).now() == null) {
            Key<Identity> key = objectify().save().entity(entity).now();
            return getIdentityByKey(key);
        } else {
            throw new EntityCollisionException("The entity being added already exists");
        }
    }

    @Override
    public Identity retrieve(String id) {
        Key<IdentityImpl> key = Key.create(IdentityImpl.class, id);
        return objectify().load().key(key).now();
    }

    private Identity getIdentityByKey(Key<Identity> key) {
        Identity foundIdentity = objectify().load().key(key).safe();
        return foundIdentity;
    }

    @Override
    public Identity update(Identity entity) throws EntityNotFoundException {
        try {
            objectify().load().entity(entity).safe();
            Key<Identity> key = objectify().save().entity(entity).now();
            return getIdentityByKey(key);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tried to load an entity without a value in the field annotated with @Id.", e);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("No entity with an id matching the value in the field annotated with @Id.", e);
        }
    }


    @Override
    public void delete(Identity entity) {
        objectify().delete().entity(entity);
    }

    @Override
    public Identity findIdentityFromProvider(String providedIdentity, String provider) {
        return retrieve(provider + ":" + providedIdentity);
    }
}
