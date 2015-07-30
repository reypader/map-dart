package com.dart.event.service;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.factory.EventFactory;
import com.dart.data.repository.EventRepository;
import com.dart.data.repository.UserRepository;
import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;
import com.dart.event.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by RMPader on 7/28/15.
 */
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventFactory eventFactory;

    public EventService(EventFactory eventFactory, EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventFactory = eventFactory;
    }

    public CreateEventResponse createEvent(CreateEventRequest request) {
        User organizer = userRepository.retrieve(request.getOrganizerId());
        Point location = new Point(request.getLocation().getLongitude(), request.getLocation().getLatitude());
        Event event = eventFactory.createEvent(organizer, request.getTitle(), request.getStartDate(), request.getEndDate(), location);
        event.setDescription(request.getDescription());
        for (String url : request.getImageURLs()) {
            event.addImageURL(url);
        }

        Event createdEvent = eventRepository.add(event);

        CreateEventResponse response = new CreateEventResponse();
        response.setEventId(createdEvent.getId());
        return response;
    }

    public FindEventResponse findEvent(String eventId) {
        Event event = eventRepository.retrieve(eventId);
        return createFindEventResponse(event);
    }

    public List<FindEventResponse> findEvents(Rectangle area) {
        Collection<Event> result = eventRepository.findUnfinishedEventsInArea(area);
        List<FindEventResponse> responses = new ArrayList<>(result.size());
        for (Event event : result) {
            responses.add(createFindEventResponse(event));
        }
        return responses;
    }

    public List<FindEventResponse> findEvents(Point center, double radius) {
        Collection<Event> result = eventRepository.findUnfinishedEventsInArea(center, radius);
        List<FindEventResponse> responses = new ArrayList<>(result.size());
        for (Event event : result) {
            responses.add(createFindEventResponse(event));
        }
        return responses;
    }

    public List<FindEventResponse> findEventsOfUser(String organizerId, int maxFinishedEvents) {
        int limit = maxFinishedEvents;
        if (limit <= 0) {
            limit = 10;
        }
        Date now = new Date();
        User organizer = userRepository.retrieve(organizerId);
        Collection<Event> resultFinished = eventRepository.findFinishedEventsByUser(organizer, now, limit);
        Collection<Event> resultUnfinished = eventRepository.findUnfinishedEventsByUser(organizer);
        List<FindEventResponse> responses = new ArrayList<>(resultFinished.size() + resultUnfinished.size());
        for (Event event : resultUnfinished) {
            responses.add(createFindEventResponse(event));
        }
        for (Event event : resultFinished) {
            responses.add(createFindEventResponse(event));
        }
        return responses;
    }

    public List<FindEventResponse> findFinishedEventsOfUserBefore(String organizerId, Date date, int maxFinishedEvents) {
        int limit = maxFinishedEvents;
        if (limit <= 0) {
            limit = 10;
        }
        User organizer = userRepository.retrieve(organizerId);
        Collection<Event> result = eventRepository.findFinishedEventsByUser(organizer, date, limit);
        List<FindEventResponse> responses = new ArrayList<>(result.size());
        for (Event event : result) {
            responses.add(createFindEventResponse(event));
        }
        return responses;
    }

    protected FindEventResponse createFindEventResponse(Event event) {
        Identity organizer = new Identity();
        organizer.setId(event.getOrganizer().getId());
        organizer.setName(event.getOrganizer().getDisplayName());
        FindEventResponse response = new FindEventResponse();
        response.setId(event.getId());
        response.setOrganizer(organizer);
        response.setDescription(event.getDescription());
        response.setStartDate(event.getStartDate());
        response.setEndDate(event.getEndDate());
        String[] urls = new String[event.getImageURLs().size()];
        response.setImageURLs(event.getImageURLs().toArray(urls));
        response.setTitle(event.getTitle());
        Location location = new Location();
        location.setLatitude(event.getLocation().getLatitude());
        location.setLongitude(event.getLocation().getLongitude());
        response.setLocation(location);
        return response;
    }


}
