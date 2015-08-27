package com.dart.common.test.domain;

import com.dart.data.domain.Registration;

import java.util.Date;
import java.util.Objects;

/**
 * @author RMPader
 */
public class DummyRegistration implements Registration {

    private String email;
    private String displayName;
    private String password;
    private String id;
    private Date dateCreated;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getPassword() {
        return password;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummyRegistration that = (DummyRegistration) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
