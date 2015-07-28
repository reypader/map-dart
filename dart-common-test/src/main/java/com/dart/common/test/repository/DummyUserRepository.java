package com.dart.common.test.repository;

import com.dart.common.test.domain.DummyUser;
import com.dart.data.domain.User;
import com.dart.data.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Dummy implementation of {@link UserRepository} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyUserRepository implements UserRepository {

    private Map<String, User> dummyStore = new HashMap<>();

    public Map<String, User> getStoredData() {
        return dummyStore;
    }

    @Override
    public User add(User entity) {
        DummyUser user = (DummyUser) entity;
        dummyStore.put(user.getId(), user);
        return user;
    }

    @Override
    public User retrieve(String id) {
        return dummyStore.get(id);
    }

    @Override
    public User update(User entity) {
        return entity;
    }

    @Override
    public void delete(User entity) {

    }
}
