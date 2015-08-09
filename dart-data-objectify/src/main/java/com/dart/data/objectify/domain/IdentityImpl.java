package com.dart.data.objectify.domain;

import com.dart.data.domain.Identity;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author RMPader
 */
@Entity(name = "Identity")
public class IdentityImpl implements Identity {

    @Id
    private String id;

    @Load
    @Index
    private Ref<User> userRef;

    private String provider;

    private String providedIdentity;

    private Date dateCreated;

    private Map<String, Object> data = new HashMap<>();

    public IdentityImpl(Key<User> userKey, String provider, String providedIdentity) {
        this.id = provider + ":" + providedIdentity;
        this.userRef = Ref.create(userKey);
        this.provider = provider;
        this.providedIdentity = providedIdentity;
    }

    @OnSave
    public void onSave() {
        if (dateCreated == null) {
            Date now = new Date();
            this.dateCreated = now;
        }
    }

    @Override
    public User getUser() {
        return userRef.get();
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public String getProvidedIdentity() {
        return providedIdentity;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public void addData(String key, Object value) {
        data.put(key, value);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityImpl identity = (IdentityImpl) o;
        return Objects.equals(getId(), identity.getId());
    }
}
