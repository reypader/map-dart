package com.dart.common.test.repository;

import com.dart.common.test.domain.DummyEvent;
import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.repository.EventRepository;

import java.util.*;

/**
 * Dummy implementation of {@link EventRepository} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyEventRepository implements EventRepository {

    private Map<String, Event> dummyStore = new HashMap<>();

    @Override
    public Collection<Event> findEventsByUser(User organizer, int limit) {
        List<Event> events = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getOrganizer().getId().equals(organizer.getId())) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public Collection<Event> findEventsByUserSince(User organizer, Date date, int limit) {
        List<Event> events = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getDateCreated().after(date) && event.getOrganizer().getId().equals(organizer.getId())) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public Collection<Event> findEventsCreatedSince(Date date, int limit) {
        List<Event> events = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getDateCreated().after(date)) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public Collection<Event> findEventsActiveOn(Date date, int limit) {
        List<Event> events = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getEndDate().after(date) && event.getStartDate().before(date)) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public Event add(Event entity) {
        DummyEvent event = (DummyEvent) entity;
        String id = UUID.randomUUID().toString();
        event.setId(id);
        dummyStore.put(id, event);
        return event;
    }

    @Override
    public Event retrieve(String id) {
        return dummyStore.get(id);
    }

    @Override
    public Event update(Event entity) {
        return entity;
    }

    @Override
    public void delete(Event entity) {

    }
}
