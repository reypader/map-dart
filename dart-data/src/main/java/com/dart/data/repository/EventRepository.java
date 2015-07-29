package com.dart.data.repository;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;

import java.util.Collection;
import java.util.Date;

/**
 * Interface for complex data access to stored {@link Event} classes.
 *
 * @author RMPader
 */
public interface EventRepository extends CrudRepository<Event> {

    /**
     * Retrieves the {@link Event} entities that are located within the given rectangular area.
     * The returned entities are those who lie within the search area and have their end dates
     * before the time of the query.
     *
     * @param area the rectangular area - defined by its NE and SW corner points to search in.
     * @return the collection of unfinished events.
     */
    Collection<Event> findUnfinishedEventsInArea(Rectangle area);

    /**
     * Retrieves the {@link Event} entities that are located within the given circle.
     * The returned entities are those who lie within the search area and have their end dates
     * before the time of the query.
     *
     * @param center the center of the circular area to search in.
     * @param radius the radius of the circle.
     * @return the collection of unfinished events.
     */
    Collection<Event> findUnfinishedEventsInArea(Point center, double radius);

    /**
     * Retrieves the {@link Event} entities that have their end dates before the time of the query
     * and have been organized by the given user.
     *
     * @param organizer the center of the circular area to search in.
     * @return the collection of unfinished events.
     */
    Collection<Event> findUnfinishedEventsByUser(User organizer);

    /**
     * Retrieves the {@link Event} entities that have their end dates after the given time
     * and have been organized by the given user.
     *
     * @param organizer the center of the circular area to search in.
     * @param date      them time when the events should have ended.
     * @param limit     the maximum number of events that will be returned.
     * @return the collection of unfinished events.
     */
    Collection<Event> findFinishedEventsByUser(User organizer, Date date, int limit);

}
