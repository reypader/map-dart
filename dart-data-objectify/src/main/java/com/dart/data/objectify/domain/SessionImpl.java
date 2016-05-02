package com.dart.data.objectify.domain;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import org.springframework.session.MapSession;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author RMPader
 */
@Entity(name = "Session")
public class SessionImpl implements Session {

    public static final String IP_ADDRESS_ATTRIBUTE = "ipAddress";
    public static final String BROWSER_ATTRIBUTE = "browser";
    public static final String DEVICE_ATTRIBUTE = "device";
    public static final String LOCATION_ATTRIBUTE = "location";

    @Id
    private String id;

    @Load
    @Index
    private Ref<User> userRef;

    private Date dateCreated;

    private MapSession mapSession;

    public SessionImpl() {
        this(new MapSession());
    }

    public SessionImpl(MapSession mapSession) {
        this.mapSession = mapSession;
        this.dateCreated = new Date(mapSession.getCreationTime());
        this.id = mapSession.getId();
        if(id == null){
            id = UUID.randomUUID().toString();
            mapSession.setId(id);
        }
    }

    @OnSave
    public void onSave() {
        if (userRef == null) {
            throw new IllegalStateException("Cannot save session without associated user");
        }
    }


    @Override
    public void setUser(User user) {
        setUser(Key.create(user));
    }

    @Override
    public User getUser() {
        return userRef.get();
    }

    public void setUser(Key<User> user) {
        if (this.userRef == null) {
            this.userRef = Ref.create(user);
        } else {
            throw new IllegalStateException("Cannot set user more than once");
        }
    }

    @Override
    public String getIPAddress() {
        return getAttribute(IP_ADDRESS_ATTRIBUTE);
    }

    @Override
    public String getDevice() {
        return getAttribute(DEVICE_ATTRIBUTE);
    }

    @Override
    public String getBrowser() {
        return getAttribute(BROWSER_ATTRIBUTE);
    }

    @Override
    public String getLocation() {
        return getAttribute(LOCATION_ATTRIBUTE);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public <T> T getAttribute(String attributeName) {
        return null;
    }

    @Override
    public Set<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {

    }

    @Override
    public void removeAttribute(String attributeName) {

    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionImpl session = (SessionImpl) o;
        return Objects.equals(id, session.id);
    }

    @Override
    public long getCreationTime() {
        return mapSession.getCreationTime();
    }

    @Override
    public long getLastAccessedTime() {
        return mapSession.getLastAccessedTime();
    }

    @Override
    public int getMaxInactiveIntervalInSeconds() {
        return mapSession.getMaxInactiveIntervalInSeconds();
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(int interval) {
        mapSession.setMaxInactiveIntervalInSeconds(interval);
    }

    @Override
    public boolean isExpired() {
        return mapSession.isExpired();
    }
}
