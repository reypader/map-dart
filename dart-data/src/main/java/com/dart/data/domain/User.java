package com.dart.data.domain;

/**
 * User entity interface that provides necessary methods
 * for performing business logic.
 *
 * @author RMPader
 */
public interface User extends Entity {

    String getDisplayName();

    void setDisplayName(String displayName);

    String getDescription();

    void setDescription(String description);

    String getPhotoURL();

    void setPhotoURL(String photoURL);

}
