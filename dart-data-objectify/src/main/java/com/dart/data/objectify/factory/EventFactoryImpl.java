package com.dart.data.objectify.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.factory.EventFactory;
import com.dart.data.objectify.domain.EventImpl;
import com.dart.data.util.Point;
import com.googlecode.objectify.Key;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by RMPader on 7/27/15.
 */
@Component
public class EventFactoryImpl implements EventFactory {

    @Override
    public Event createEvent(User organizer, String title, Date startDate, Date endDate, Point location) {
        return new EventImpl(Key.create(organizer), title, startDate, endDate, location);
    }
}
