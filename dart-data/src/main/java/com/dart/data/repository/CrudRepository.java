package com.dart.data.repository;

import com.dart.data.domain.Entity;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;

/**
 * Base interface for entity repositories. Provides basic CRUD methods.
 *
 * @author RMPader
 */
public interface CrudRepository<T extends Entity> {

    T add(T entity) throws EntityCollisionException;

    /**
     * @param id the string representation of the entity's ID.
     * @return the retrieved entity.
     */
    T retrieve(String id);

    T update(T entity) throws EntityNotFoundException;

    void delete(T entity);
}
