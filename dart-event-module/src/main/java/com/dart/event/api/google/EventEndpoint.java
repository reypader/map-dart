package com.dart.event.api.google;

import com.dart.event.api.CreateEventRequest;
import com.dart.event.api.CreateEventResponse;
import com.dart.event.api.FindEventResponse;
import com.dart.event.service.EventService;

import java.util.Date;

/**
 * Created by RMPader on 7/28/15.
 */
public class EventEndpoint {

    private EventService eventService;

    public EventEndpoint(EventService eventService) {
        this.eventService = eventService;
    }

    public CreateEventResponse createEvent(CreateEventRequest request) {
        return eventService.createEvent(request);
    }

    public FindEventResponse findEvent(String eventId) {
        return eventService.findEvent(eventId);
    }

    public FindEventResponse[] findEventsOfUser(String userId, Date since) {
        return null;
    }

    public FindEventResponse[] findEvents(Date since) {
        return null;
    }
}
