package com.dart.data.objectify.repository;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.SessionImpl;
import com.dart.data.repository.SessionRepository;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Ref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.dart.data.objectify.ObjectifyProvider.objectify;

/**
 * Created by RMPader on 7/25/15.
 */
public class SessionRepositoryImpl implements SessionRepository {
    static {
        ObjectifyProvider.register(SessionImpl.class);
    }

    @Override
    public Session add(Session entity) {
        if (objectify().load().entity(entity).now() == null) {
            Key<Session> key = objectify().save().entity(entity).now();
            return getSessionByKey(key);
        } else {
            throw new EntityCollisionException("The entity being added already exists");
        }
    }

    @Override
    public Session retrieve(String id) {
        Key<SessionImpl> key = Key.create(SessionImpl.class, id);
        return objectify().load().key(key).now();
    }

    private Session getSessionByKey(Key<Session> key) {
        Session foundSession = objectify().load().key(key).safe();
        return foundSession;
    }

    @Override
    public Session update(Session entity) throws EntityNotFoundException {
        try {
            objectify().load().entity(entity).safe();
            Key<Session> key = objectify().save().entity(entity).now();
            return getSessionByKey(key);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tried to load an entity without a value in the field annotated with @Id.", e);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("No entity with an id matching the value in the field annotated with @Id.", e);
        }
    }

    @Override
    public void delete(Session entity) {
        objectify().delete().entity(entity);
    }


    @Override
    public Collection<Session> findSessionsOfUser(User user) {
        Collection<SessionImpl> result = objectify().load().type(SessionImpl.class).filter("userRef", Ref.create(user)).list();
        List<Session> sessions = new ArrayList<>(result.size());
        sessions.addAll(result);
        return sessions;
    }
}
