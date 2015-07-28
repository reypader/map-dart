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

    public Map<String, Event> getStoredData() {
        return dummyStore;
    }

    @Override
    public Collection<Event> findEventsByUserBefore(User organizer, Date date, int limit) {
        List<Event> events = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getDateCreated().before(date) && event.getOrganizer().getId().equals(organizer.getId())) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public Collection<Event> findEventsCreatedBefore(Date date, int limit) {
        List<Event> events = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getDateCreated().before(date)) {
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
