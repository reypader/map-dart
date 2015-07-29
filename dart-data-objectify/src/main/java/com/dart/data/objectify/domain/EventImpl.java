package com.dart.data.objectify.domain;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.util.Point;
import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

import java.util.*;

/**
 * {@inheritDoc}
 */
@Entity(name = "Event")
public class EventImpl implements Event {

    /**
     * DataStore ID automatically populated by Objectify.
     */
    @Id
    private Long id;

    @Load
    @Index
    private Ref<User> userRef;

    private List<String> imageURLs = new ArrayList<>();

    private Date dateCreated;

    @Index
    private Date startDate;

    @Index
    private Date endDate;

    @Index
    private GeoPt location;

    private String title;

    private String description;

    /*
     * (non-javadoc)
     *
     * placed to to help geo-spatial queries. This is meant to be updated the next time this event is
     * retrieved and is identified as finished. Upon identification, a finished event will have its "isFinished" field
     * set to true and persisted.
     */
    @Index
    private boolean isFinished;

    public EventImpl(Key<User> organizerKey, String title, Date startDate, Date endDate, Point location) {
        this.userRef = Ref.create(organizerKey);
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setLocation(location);
    }

    @OnSave
    public void onSave() {
        Date now = new Date();
        if (dateCreated == null) {
            this.dateCreated = now;
        }
        isFinished = endDate.before(now);
    }

    /**
     * The real ID cannot be used to query since this entity is being saved with a parent. Hence, we instead return a
     * representation of the Key containing the full ancestral path of this entity.
     *
     * @return a web-safe representation of the Datastore Key for this entity.
     */
    @Override
    public String getId() {
        return Key.create(this).toWebSafeString();
    }

    @Override
    public User getOrganizer() {
        return userRef.get();
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void addImageURL(String url) {
        this.imageURLs.add(url);
    }

    @Override
    public Collection<String> getImageURLs() {
        return imageURLs;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setStartDate(Date date) {
        this.startDate = date;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setEndDate(Date date) {
        this.endDate = date;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setLocation(Point location) {
        this.location = new GeoPt(location.getLatitude(), location.getLongitude());
    }

    @Override
    public Point getLocation() {
        return new Point(location.getLongitude(), location.getLatitude());
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventImpl event = (EventImpl) o;
        return Objects.equals(getId(), event.getId());
    }


}
