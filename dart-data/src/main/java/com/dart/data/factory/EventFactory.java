package com.dart.data.factory;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;

import java.util.Date;

/**
 * Created by RMPader on 7/27/15.
 */
public interface EventFactory {

    Event createEvent(User organizer, String title, Date startDate, Date endDate);

}
