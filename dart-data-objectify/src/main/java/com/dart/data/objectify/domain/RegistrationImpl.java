package com.dart.data.objectify.domain;

import com.dart.data.domain.Registration;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.OnSave;

import java.util.Date;
import java.util.Objects;

/**
 * @author RMPader
 */
@Entity(name = "Registration")
public class RegistrationImpl implements Registration {

    @Id
    private String registrationCode;

    private String email;

    private String displayName;

    private String password;

    private Date dateCreated;

    public RegistrationImpl(String registrationCode, String email, String displayName, String password) {
        this.registrationCode = registrationCode;
        this.email = email;
        this.displayName = displayName;
        this.password = password;
    }

    @OnSave
    public void onSave() {
        if (dateCreated == null) {
            Date now = new Date();
            this.dateCreated = now;
        }
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
    public String getPassword() {
        return password;
    }

    @Override
    public String getId() {
        return registrationCode;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationImpl that = (RegistrationImpl) o;
        return Objects.equals(registrationCode, that.registrationCode);
    }
}
