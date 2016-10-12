package com.dart.event.api.google;

import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;
import com.dart.event.api.CreateEventRequest;
import com.dart.event.api.CreateEventResponse;
import com.dart.event.api.FindEventResponse;
import com.dart.event.service.EventService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

/**
 * Created by RMPader on 7/28/15.
 */
@Api(name = "event")
public class EventEndpoint {

    private EventService eventService;

    @Inject
    public EventEndpoint(EventService eventService) {
        this.eventService = eventService;
    }

    @ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST)
    public CreateEventResponse createEvent(CreateEventRequest request) {
        return eventService.createEvent(request);
    }

    @ApiMethod(name = "get", path = "find/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public FindEventResponse findEvent(@Named("id") String eventId) {
        return eventService.findEvent(eventId);
    }

    @ApiMethod(name = "ofUser", path = "user/{userId}", httpMethod = ApiMethod.HttpMethod.GET)
    public List<FindEventResponse> findEventsOfUser(@Named("userId") String userId, @Named("limit") int limit) {
        return eventService.findEventsOfUser(userId, limit);
    }

    @ApiMethod(name = "finishedOfUser", path = "user-finished/{userId}", httpMethod = ApiMethod.HttpMethod.GET)
    public List<FindEventResponse> findFinishedEventsOfUserBefore(@Named("userId") String userId, @Named("asOf") Date date, @Named("limit") int limit) {
        return eventService.findFinishedEventsOfUserBefore(userId, date, limit);
    }

    @ApiMethod(name = "findAreaRectangle", path = "area/rectangle", httpMethod = ApiMethod.HttpMethod.GET)
    public List<FindEventResponse> findEventsInRectangle(Rectangle area) {
        return eventService.findEvents(area);
    }

    @ApiMethod(name = "findAreaCircle", path = "area/circle/{radius}", httpMethod = ApiMethod.HttpMethod.GET)
    public List<FindEventResponse> findEventsInCircle(Point center, @Named("radius") double radius) {
        return eventService.findEvents(center, radius);
    }
}
