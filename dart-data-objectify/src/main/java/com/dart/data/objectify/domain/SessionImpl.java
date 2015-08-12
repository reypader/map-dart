package com.dart.data.objectify.domain;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

import java.net.InetAddress;
import java.util.Date;
import java.util.Objects;

/**
 * @author RMPader
 */
@Entity(name = "Session")
public class SessionImpl implements Session {

    @Id
    private String token;

    @Load
    @Index
    private Ref<User> userRef;

    private Date expiry;

    private String ipAddress;

    private String browser;

    private String device;

    private String location;

    private Date dateCreated;

    public SessionImpl(String token, Key<User> user, String ipAddress, Date expiry, String device, String browser, String location) {
        this.token = token;
        this.userRef = Ref.create(user);
        this.expiry = expiry;
        this.device = device;
        this.browser = browser;
        this.location = location;
        this.ipAddress = ipAddress;
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
    public Date getExpiry() {
        return expiry;
    }

    @Override
    public String getIPAddress() {
        return ipAddress;
    }

    @Override
    public String getDevice() {
        return device;
    }

    @Override
    public String getBrowser() {
        return browser;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getId() {
        return token;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionImpl session = (SessionImpl) o;
        return Objects.equals(token, session.token);
    }

}
