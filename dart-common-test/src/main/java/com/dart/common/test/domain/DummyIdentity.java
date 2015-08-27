package com.dart.common.test.domain;

import com.dart.data.domain.Identity;
import com.dart.data.domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author RMPader
 */
public class DummyIdentity implements Identity {

    private User user;
    private String provider;
    private String providedIdentity;
    private Map<String, Object> data = new HashMap<>();
    private String id;
    private Date dateCreated;

    @Override
    public User getUser() {
        return user;
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

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProvidedIdentity(String providedIdentity) {
        this.providedIdentity = providedIdentity;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummyIdentity that = (DummyIdentity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
