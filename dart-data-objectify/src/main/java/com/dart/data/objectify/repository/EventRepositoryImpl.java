package com.dart.data.objectify.repository;

import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.domain.Event;
import com.dart.data.repository.EventRepository;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.EventImpl;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.LoadType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.dart.data.objectify.ObjectifyProvider.objectify;

/**
 * {@inheritDoc}
 */
public class EventRepositoryImpl implements EventRepository {

    static {
        ObjectifyProvider.register(EventImpl.class);
    }

    @Override
    public Event add(Event entity) {
        Key<Event> key = objectify().save().entity(entity).now();
        return getEventByKey(key);
    }

    @Override
    public Event update(Event entity) {
        ensureExistingEvent(entity);
        return add(entity);
    }

    private void ensureExistingEvent(Event event) {
        try {
            objectify().load().entity(event).safe();
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tried to load an entity without a value in the field annotated with @Id.", e);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("No entity with an id matching the value in the field annotated with @Id.", e);
        }
    }

    @Override
    public Event retrieve(String id) {
        Key<Event> key = Key.create(id);
        return getEventByKey(key);
    }

    private Event getEventByKey(Key<Event> key) {
        Event foundEvent = objectify().load().key(key).safe();
        return foundEvent;
    }

    @Override
    public void delete(Event entity) {
        objectify().delete().entity(entity);
    }

    @Override
    public Collection<Event> findEventsByUser(User organizer, int limit) {
        List<EventImpl> result = loadEvent().filter("userRef", Ref.create(organizer)).limit(limit).list();
        List<Event> events = new ArrayList<>();
        events.addAll(result);
        return events;
    }

    @Override
    public Collection<Event> findEventsByUserSince(User organizer, Date date, int limit) {
        List<EventImpl> result = loadEvent().filter("userRef", Ref.create(organizer)).filter("dateCreated >=", date).limit(limit).list();
        List<Event> events = new ArrayList<>();
        events.addAll(result);
        return events;
    }

    @Override
    public Collection<Event> findEventsCreatedSince(Date date, int limit) {
        List<EventImpl> result = loadEvent().filter("dateCreated >=", date).limit(limit).list();
        List<Event> events = new ArrayList<>();
        events.addAll(result);
        return events;
    }

    @Override
    public Collection<Event> findEventsActiveOn(Date date, int limit) {
        List<EventImpl> result = loadEvent().filter("startDate <=", date).limit(limit).list();
        List<Event> events = new ArrayList<>();
        // Datastore does not support multiple inequality as of July 2015.
        // We are essentially doing: filter("endDate >", date).
        for (Event event : result) {
            if (event.getEndDate().after(date)) {
                events.add(event);
            }
        }
        return events;
    }

    private LoadType<EventImpl> loadEvent() {
        return objectify().load().type(EventImpl.class);
    }
}
