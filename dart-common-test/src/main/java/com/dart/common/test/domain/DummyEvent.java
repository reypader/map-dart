package com.dart.common.test.domain;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.util.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Dummy implementation of {@link Event} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyEvent implements Event {

    private User organizer;
    private List<String> imageURLs = new ArrayList<>();
    private String title;
    private String description;
    private Date dateCreated;
    private Point location;
    private String id;
    private Date endDate;
    private Date startDate;

    @Override
    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User user) {
        this.organizer = user;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date date) {
        this.startDate = date;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date date) {
        this.endDate = date;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }
}
