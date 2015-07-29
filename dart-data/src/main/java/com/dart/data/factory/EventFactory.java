package com.dart.data.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.util.Point;

import java.util.Date;

/**
 * Factory that allows creation of Event classes
 * in place of concrete constructors.
 *
 * @author RMPader
 */
public interface EventFactory {


    /**
     * Method for creating a event with the minimum required fields.
     *
     * @param organizer the {@link User} that organized this event.
     * @param title     the title of this event.
     * @param startDate the {@link Date} when the event will start.
     * @param endDate   the {@link Date} when the event will end.
     * @param location  the geographical location of the event.
     * @return the newly created {@link Event} instance.
     */
    Event createEvent(User organizer, String title, Date startDate, Date endDate, Point location);

}
