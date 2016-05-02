package com.dart.common.service.auth.session;

import com.dart.data.domain.Session;
import com.dart.data.factory.SessionFactory;
import com.dart.data.repository.SessionRepository;

/**
 * Created by rpader on 4/30/16.
 */
public class GaeSessionRepository implements org.springframework.session.SessionRepository<Session> {

    private SessionRepository sessionRepository;
    private SessionFactory sessionFactory;

    public GaeSessionRepository(SessionRepository sessionRepository, SessionFactory sessionFactory) {
        this.sessionRepository = sessionRepository;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session createSession() {
        return sessionFactory.createSession();
    }

    @Override
    public void save(Session session) {
        sessionRepository.add(session);
    }

    @Override
    public Session getSession(String id) {
        return sessionRepository.retrieve(id);
    }

    @Override
    public void delete(String id) {
        Session session = getSession(id);
        sessionRepository.delete(session);
    }
}
