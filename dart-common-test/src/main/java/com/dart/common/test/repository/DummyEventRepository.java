package com.dart.common.test.repository;

import com.dart.common.test.domain.DummyEvent;
import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.repository.EventRepository;
import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;

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
    public Event update(Event entity) throws EntityNotFoundException {
        if (dummyStore.get(entity.getId()) == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        dummyStore.remove(entity.getId());
        dummyStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Event entity) {
        dummyStore.remove(entity.getId());
    }


    @Override
    public Collection<Event> findUnfinishedEventsInArea(Rectangle area) {

        List<Event> result = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            Point p = new Point(event.getLocation().getX(), event.getLocation().getY());
            if (event.getEndDate().after(new Date()) && contains(area, p)) {
                result.add(event);
            }
        }
        return result;
    }

    private boolean contains(Rectangle area, Point p) {
        boolean x = (Float.compare(p.getX(), area.getSouthWestCorner().getX()) > 0) && (Float.compare(p.getX(), area.getNorthEastCorner().getX()) < 0);
        boolean y = (Float.compare(p.getY(), area.getSouthWestCorner().getY()) > 0) && (Float.compare(p.getY(), area.getNorthEastCorner().getY()) < 0);
        return x && y;
    }

    @Override
    public Collection<Event> findUnfinishedEventsInArea(Point center, double radius) {
        List<Event> result = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            Point p = new Point(event.getLocation().getX(), event.getLocation().getY());
            if (event.getEndDate().after(new Date()) && contains(p, center, radius)) {
                result.add(event);
            }
        }
        return result;
    }

    private boolean contains(Point p, Point center, double radius) {
        float dist = distFrom(p.getLatitude(), p.getLongitude(), center.getLatitude(), center.getLongitude());
        return dist < radius;
    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    @Override
    public Collection<Event> findUnfinishedEventsByUser(User organizer) {
        List<Event> result = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getEndDate().after(new Date()) && event.getOrganizer().getId().equals(organizer.getId())) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public Collection<Event> findFinishedEventsByUser(User organizer, Date date, int limit) {
        List<Event> events = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            if (event.getEndDate().before(date) && event.getOrganizer().getId().equals(organizer.getId())) {
                events.add(event);
            }
            if (events.size() >= limit) {
                return events;
            }
        }
        return events;
    }
}
