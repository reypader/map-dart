package com.dart.common.test.domain;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;

import java.util.Date;
import java.util.Objects;

/**
 * @author RMPader
 */
public class DummySession implements Session {

    private User user;
    private Date expiry;
    private String device;
    private String browser;
    private String location;
    private String id;
    private Date dateCreated;
    private String IPAddress;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Date getExpiry() {
        return expiry;
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
        return id;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummySession that = (DummySession) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    @Override
    public String getIPAddress() {
        return IPAddress;
    }
}
