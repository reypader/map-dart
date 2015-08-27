package com.dart.data.objectify.repository;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.ObjectifyProvider;
import com.dart.data.objectify.domain.EventImpl;
import com.dart.data.repository.EventRepository;
import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Query;
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
        if (((EventImpl) entity).getRawId() == null) {
            Key<Event> key = objectify().save().entity(entity).now();
            return getEventByKey(key);
        } else {
            throw new EntityCollisionException("The entity being added already has an ID. Did you mean to do an update?");
        }
    }

    @Override
    public Event update(Event entity) throws EntityNotFoundException {
        try {
            objectify().load().entity(entity).safe();
            Key<Event> key = objectify().save().entity(entity).now();
            return getEventByKey(key);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tried to load an entity without a value in the field annotated with @Id.", e);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("No entity with an id matching the value in the field annotated with @Id.", e);
        }
    }

    @Override
    public Event retrieve(String id) {
        Key<Event> key = Key.create(id);
        EventImpl event = (EventImpl) objectify().load().key(key).now();
        if (event != null) {
            correctFinishedIndicator(event);
        }
        return event;
    }

    private void correctFinishedIndicator(EventImpl event) {
        try {
            Date now = new Date();
            if (!event.isFinished() && now.after(event.getEndDate())) {
                update(event);
            }
        } catch (EntityNotFoundException e) {
            throw new IllegalStateException("The Event being corrected seems to have vanished", e);
        }
    }

    private EventImpl getEventByKey(Key<Event> key) {
        EventImpl foundEvent = (EventImpl) objectify().load().key(key).safe();
        return foundEvent;
    }

    @Override
    public void delete(Event entity) {
        objectify().delete().entity(entity);
    }


    private LoadType<EventImpl> loadEvent() {
        return objectify().load().type(EventImpl.class);
    }

    @Override
    public Collection<Event> findUnfinishedEventsInArea(Rectangle area) {
        GeoPt southwest = new GeoPt(area.getSouthWestCorner().getLatitude(), area.getSouthWestCorner().getLongitude());
        GeoPt northeast = new GeoPt(area.getNorthEastCorner().getLatitude(), area.getNorthEastCorner().getLongitude());
        Query.Filter searchArea = new Query.StContainsFilter("location", new Query.GeoRegion.Rectangle(southwest, northeast));

        Collection<EventImpl> result = loadEvent().filter(searchArea).filter("isFinished", false).list();
        List<Event> events = getUnfinishedEvents(result);
        return events;
    }

    @Override
    public Collection<Event> findUnfinishedEventsInArea(Point center, double radius) {
        GeoPt gptCenter = new GeoPt(center.getLatitude(), center.getLongitude());
        Query.Filter searchArea = new Query.StContainsFilter("location", new Query.GeoRegion.Circle(gptCenter, radius));
        Collection<EventImpl> result = loadEvent().filter(searchArea).list();
        List<Event> events = getUnfinishedEvents(result);
        return events;
    }

    private List<Event> getUnfinishedEvents(Collection<EventImpl> result) {
        List<Event> events = new ArrayList<>();
        Date now = new Date();
        for (EventImpl event : result) {
            if (event.getEndDate().after(now)) {
                events.add(event);
            } else {
                correctFinishedIndicator(event);
            }
        }
        return events;
    }

    @Override
    public Collection<Event> findUnfinishedEventsByUser(User organizer) {
        Collection<EventImpl> result = loadEvent().filter("userRef", Ref.create(organizer)).filter("endDate >", new Date()).list();
        List<Event> events = new ArrayList<>(result.size());
        events.addAll(result);
        return events;
    }

    @Override
    public Collection<Event> findFinishedEventsByUser(User organizer, Date date, int limit) {
        Collection<EventImpl> result = loadEvent().filter("userRef", Ref.create(organizer)).filter("endDate <=", date).limit(limit).list();
        List<Event> events = new ArrayList<>(result.size());
        for (EventImpl event : result) {
            events.add(event);
            correctFinishedIndicator(event);
        }
        return events;
    }
}
