package com.dart.common.test.repository;

import com.dart.common.test.domain.DummyRegistration;
import com.dart.data.domain.Registration;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.repository.RegistrationRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Dummy implementation of {@link RegistrationRepository} class for unit testing. Use Mockito.spy() to verify
 * invocations.
 */
public class DummyRegistrationRepository implements RegistrationRepository {

    private Map<String, Registration> dummyStore = new HashMap<>();

    public Map<String, Registration> getStoredData() {
        return dummyStore;
    }

    @Override
    public Registration add(Registration entity) {
        if (dummyStore.get(entity.getId()) != null) {
            throw new EntityCollisionException("Entity already exists");
        }
        DummyRegistration registration = (DummyRegistration) entity;
        dummyStore.put(registration.getId(), registration);
        return registration;
    }

    @Override
    public Registration retrieve(String id) {
        return dummyStore.get(id);
    }

    @Override
    public Registration update(Registration entity) throws EntityNotFoundException {
        if (dummyStore.get(entity.getId()) == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        dummyStore.remove(entity.getId());
        dummyStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Registration entity) {
        dummyStore.remove(entity.getId());
    }

    @Override
    public void deleteRegistrationForEmail(String email) {
        for (Registration registration : dummyStore.values()) {
            if (registration.getEmail().equals(email)) {
                dummyStore.remove(registration.getId());
            }
        }
    }
}
