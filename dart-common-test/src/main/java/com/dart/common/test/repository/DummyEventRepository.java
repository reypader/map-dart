package com.dart.common.test.repository;

import com.dart.common.test.domain.DummyEvent;
import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.repository.EventRepository;
import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    public Event update(Event entity) {
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
        float width = Math.abs(area.getNorthEastCorner().getX() - area.getSouthWestCorner().getX());
        float height = Math.abs(area.getNorthEastCorner().getY() - area.getSouthWestCorner().getY());
        Rectangle2D rect = new Rectangle2D.Float(area.getSouthWestCorner().getX(), area.getNorthEastCorner().getY(), width, height);
        List<Event> result = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            Point2D.Float p = new Point2D.Float(event.getLocation().getX(), event.getLocation().getY());
            if (event.getEndDate().after(new Date()) && rect.contains(p)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public Collection<Event> findUnfinishedEventsInArea(Point center, double radius) {
        Ellipse2D.Float circle = new Ellipse2D.Float(center.getX(), center.getY(), (float) radius, (float) radius);
        List<Event> result = new ArrayList<>();
        for (Event event : dummyStore.values()) {
            Point2D.Float p = new Point2D.Float(event.getLocation().getX(), event.getLocation().getY());
            if (event.getEndDate().after(new Date()) && circle.contains(p)) {
                result.add(event);
            }
        }
        return result;
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
