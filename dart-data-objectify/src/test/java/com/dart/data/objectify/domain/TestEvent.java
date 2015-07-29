package com.dart.data.objectify.domain;

import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.util.Point;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

import java.util.Collection;
import java.util.Date;

/**
 * Created by RMPader on 7/27/15.
 */
@Entity(name = "Event")
public class TestEvent implements Event {

    @Id
    private Long id;

    @Parent
    private Ref<User> user;

    public TestEvent(Key<User> parent) {
        user = Ref.create(parent);
    }

    @Override
    public User getOrganizer() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void addImageURL(String url) {

    }

    @Override
    public Collection<String> getImageURLs() {
        return null;
    }

    @Override
    public Date getDateCreated() {
        return null;
    }

    @Override
    public void setStartDate(Date date) {

    }

    @Override
    public Date getStartDate() {
        return null;
    }

    @Override
    public void setEndDate(Date date) {

    }

    @Override
    public Date getEndDate() {
        return null;
    }

    @Override
    public void setLocation(Point location) {

    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }
}
