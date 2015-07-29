package com.dart.data.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;

import java.util.Date;

/**
 * Factory that allows creation of Event classes
 * in place of concrete constructors.
 *
 * @author RMPader
 */
public interface EventFactory {

    Event createEvent(User organizer, String title, Date startDate, Date endDate);

}
