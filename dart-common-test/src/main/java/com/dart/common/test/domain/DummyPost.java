package com.dart.common.test.domain;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Dummy implementation of {@link Post} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyPost implements Post {
    private List<String> imageURLs = new ArrayList<>();
    private Event event;
    private User user;
    private String content;
    private String id;
    private Date dateCreated;

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public Event getEvent() {
        return event;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
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
