package com.dart.common.test.domain;

import com.dart.data.domain.Session;
import com.dart.data.domain.User;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DummySession that = (DummySession) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {

    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public int getMaxInactiveIntervalInSeconds() {
        return 0;
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(int interval) {

    }

    @Override
    public boolean isExpired() {
        return false;
    }
}
