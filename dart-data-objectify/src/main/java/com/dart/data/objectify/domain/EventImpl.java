package com.dart.data.objectify.domain;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    @Index
    private Date dateCreated;

    @Index
    private Date startDate;

    @Index
    private Date endDate;

    private String title;

    private String description;

    public EventImpl(Key<User> organizerKey, String title, Date startDate, Date endDate) {
        this.userRef = Ref.create(organizerKey);
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    @OnSave
    public void onSave() {
        this.dateCreated = new Date();
    }

    /**
     * The real ID cannot be used to query since this entity is being saved with a parent.
     * Hence, we instead return a representation of the Key containing the full ancestral path of this entity.
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


}
