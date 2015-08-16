package com.dart.data.objectify.domain;

import com.dart.data.domain.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * Created by RMPader on 7/27/15.
 */
@Entity(name = "User")
public class TestUser implements User {

    @Id
    private String username;

    public TestUser(String id) {
        this.username = id;
    }

    @Override
    public String getId() {
        return this.username;
    }

    @Override
    public Date getDateCreated() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public String getPhotoURL() {
        return null;
    }

    @Override
    public void setPhotoURL(String photoURL) {

    }
}
