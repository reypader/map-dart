package com.dart.data.objectify.domain;

import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

import java.util.*;

/**
 * Created by RMPader on 7/25/15.
 */
@Entity(name = "Post")
public class PostImpl implements Post {

    /**
     * DataStore ID automatically populated by Objectify if the type is {@link Long}.
     */
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
        if (dateCreated == null) {
            Date now = new Date();
            this.dateCreated = now;
        }
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

    /**
     * for checking since getId() will throw an error if the entity is new (i.e. not yet persisted and thus, no ID
     * assigned yet)
     *
     * @return
     */
    public Long getRawId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostImpl post = (PostImpl) o;
        return Objects.equals(getId(), post.getId());
    }
}
