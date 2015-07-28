package com.dart.data.repository;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;

import java.util.Collection;
import java.util.Date;

/**
 * Created by RMPader on 7/25/15.
 */
public interface EventRepository extends CrudRepository<Event> {

    Collection<Event> findEventsByUserBefore(User organizer, Date date, int limit);

    Collection<Event> findEventsCreatedBefore(Date date, int limit);

    Collection<Event> findEventsActiveOn(Date date, int limit);

}
