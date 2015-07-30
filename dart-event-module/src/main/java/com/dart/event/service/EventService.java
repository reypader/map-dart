package com.dart.event.service;

import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;
import com.dart.event.api.CreateEventRequest;
import com.dart.event.api.CreateEventResponse;
import com.dart.event.api.FindEventResponse;

import java.util.Date;
import java.util.List;

/**
 * @author RMPader
 */
public interface EventService {
    CreateEventResponse createEvent(CreateEventRequest request);

    FindEventResponse findEvent(String eventId);

    List<FindEventResponse> findEvents(Rectangle area);

    List<FindEventResponse> findEvents(Point center, double radius);

    List<FindEventResponse> findEventsOfUser(String organizerId, int maxFinishedEvents);

    List<FindEventResponse> findFinishedEventsOfUserBefore(String organizerId, Date date, int maxFinishedEvents);
}
