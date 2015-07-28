package com.dart.common.test.domain;

import com.dart.data.domain.User;

import java.util.Date;

/**
 * Dummy implementation of {@link User} class for unit testing. Use Mockito.spy() to verify invocations.
 */
public class DummyUser implements User {
    private String id;
    private String displayName;
    private String description;
    private String photoURL;
    private Date dateCreated;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
    public String getPhotoURL() {
        return photoURL;
    }

    @Override
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
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
