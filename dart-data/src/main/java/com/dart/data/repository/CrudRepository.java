package com.dart.data.repository;

import com.dart.data.domain.Entity;

/**
 * Created by RMPader on 7/26/15.
 */
public interface CrudRepository<T extends Entity> {

    T add(T entity);

    /**
     * @param id - the string representation of the ID.
     * @return
     */
    T retrieve(String id);

    T update(T entity);

    void delete(T entity);
}
