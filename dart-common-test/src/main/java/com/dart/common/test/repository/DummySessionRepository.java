package com.dart.common.test.repository;

import com.dart.common.test.domain.DummySession;
import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.dart.data.repository.SessionRepository;

import java.util.*;

/**
 * Dummy implementation of {@link SessionRepository} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummySessionRepository implements SessionRepository {

    private Map<String, Session> dummyStore = new HashMap<>();

    public Map<String, Session> getStoredData() {
        return dummyStore;
    }

    @Override
    public Session add(Session entity) {
        DummySession session = (DummySession) entity;
        dummyStore.put(session.getId(), session);
        return session;
    }

    @Override
    public Session retrieve(String id) {
        return dummyStore.get(id);
    }

    @Override
    public Session update(Session entity) {
        dummyStore.remove(entity.getId());
        dummyStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Session entity) {
        dummyStore.remove(entity.getId());
    }

    @Override
    public Collection<Session> findSessionsOfUser(User user) {
        List<Session> sessions = new ArrayList<>();
        for (Session session : dummyStore.values()) {
            if (session.getUser().getId().equals(user.getId())) {
                sessions.add(session);
            }
        }
        return sessions;
    }
}
