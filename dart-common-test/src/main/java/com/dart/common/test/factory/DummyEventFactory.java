package com.dart.common.test.factory;

import com.dart.common.test.domain.DummyEvent;
import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.factory.EventFactory;
import com.dart.data.util.Point;

import java.util.Date;

/**
 * Dummy implementation of {@link EventFactory} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyEventFactory implements EventFactory {
    @Override
    public Event createEvent(User organizer, String title, Date startDate, Date endDate, Point location) {
        DummyEvent instance = new DummyEvent();
        instance.setDateCreated(new Date());
        instance.setOrganizer(organizer);
        instance.setTitle(title);
        instance.setStartDate(startDate);
        instance.setEndDate(endDate);
        instance.setLocation(location);
        return instance;
    }
}
