package com.dart.data.repository;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;

import java.util.Collection;
import java.util.Date;

/**
 * Interface for complex data access to stored {@link Event} classes.
 *
 * @author RMPader
 */
public interface EventRepository extends CrudRepository<Event> {

    Collection<Event> findEventsByUserBefore(User organizer, Date date, int limit);

    Collection<Event> findEventsCreatedBefore(Date date, int limit);

    Collection<Event> findEventsActiveOn(Date date, int limit);

}
