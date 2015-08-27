package com.dart.common.test.repository;

import com.dart.common.test.domain.DummyIdentity;
import com.dart.data.domain.Identity;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.repository.IdentityRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Dummy implementation of {@link IdentityRepository} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyIdentityRepository implements IdentityRepository {

    private Map<String, Identity> dummyStore = new HashMap<>();

    public Map<String, Identity> getStoredData() {
        return dummyStore;
    }

    @Override
    public Identity add(Identity entity) {
        if (dummyStore.get(entity.getId()) != null) {
            throw new EntityCollisionException("Entity already exists");
        }
        DummyIdentity identity = (DummyIdentity) entity;
        dummyStore.put(identity.getId(), identity);
        return identity;
    }

    @Override
    public Identity retrieve(String id) {
        return dummyStore.get(id);
    }

    @Override
    public Identity update(Identity entity) throws EntityNotFoundException {
        if (dummyStore.get(entity.getId()) == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        dummyStore.remove(entity.getId());
        dummyStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Identity entity) {
        dummyStore.remove(entity.getId());
    }

    @Override
    public Identity findIdentityFromProvider(String providedIdentity, String provider) {
        for (Identity identity : dummyStore.values()) {
            if (identity.getProvidedIdentity().equals(providedIdentity) && identity.getProvider().equals(provider)) {
                return identity;
            }
        }
        return null;
    }
}
