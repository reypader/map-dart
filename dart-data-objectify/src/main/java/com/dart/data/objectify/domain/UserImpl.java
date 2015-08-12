package com.dart.data.objectify.domain;

import com.dart.data.domain.User;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.OnSave;

import java.util.Date;
import java.util.Objects;

/**
 * Created by RMPader on 7/25/15.
 */
@Entity(name = "User")
public class UserImpl implements User {

    @Id
    private String id;

    private String email;

    private String displayName;

    private Date dateCreated;

    private String photoURL;

    private String description;

    private String secret;

    public UserImpl(){}

    public UserImpl(String email, String displayName) {
        this.id = DigestUtils.sha256Hex(email);
        this.email = email;
        this.setDisplayName(displayName);
    }

    @OnSave
    public void onSave() {
        if (dateCreated == null) {
            Date now = new Date();
            this.dateCreated = now;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public String getEmail() {
        return email;
    }

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
    public String getSecret() {
        return secret;
    }

    @Override
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImpl user = (UserImpl) o;
        return Objects.equals(getId(), user.getId());
    }

}
