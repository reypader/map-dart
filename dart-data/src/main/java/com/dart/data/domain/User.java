package com.dart.data.domain;

/**
 * Created by RMPader on 7/25/15.
 */
public interface User extends Entity {

    String getDisplayName();

    void setDisplayName(String displayName);

    String getDescription();

    void setDescription(String description);

    String getPhotoURL();

    void setPhotoURL(String photoURL);

}
