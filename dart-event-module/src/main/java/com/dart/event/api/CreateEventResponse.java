package com.dart.event.api;

import java.util.Objects;

/**
 * Created by RMPader on 7/28/15.
 */
public class CreateEventResponse {

    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateEventResponse that = (CreateEventResponse) o;
        return Objects.equals(getEventId(), that.getEventId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId());
    }
}
