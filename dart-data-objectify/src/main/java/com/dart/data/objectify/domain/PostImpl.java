package com.dart.data.objectify.domain;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by RMPader on 7/25/15.
 */
@Entity(name = "Post")
public class PostImpl implements Post {

    @Id
    private Long id;

    @Load
    @Index
    private Ref<Event> eventRef;

    @Load
    @Index
    private Ref<User> userRef;

    private String content;

    @Index
    private Date dateCreated;

    private List<String> imageURLs = new ArrayList<>();

    public PostImpl(Key<Event> eventKey, Key<User> userKey, String content) {
        this.eventRef = Ref.create(eventKey);
        this.userRef = Ref.create(userKey);
        this.setContent(content);
    }

    @OnSave
    public void onSave() {
        this.dateCreated = new Date();
    }

    @Override
    public Event getEvent() {
        return eventRef.get();
    }

    @Override
    public User getUser() {
        return userRef.get();
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
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public String getId() {
        return Key.create(this).toWebSafeString();
    }
}
