package com.dart.data.domain;

import java.util.Collection;

/**
 * Created by RMPader on 7/25/15.
 */
public interface Post extends Entity {

    Event getEvent();

    User getUser();

    void setContent(String content);

    String getContent();

    void addImageURL(String url);

    Collection<String> getImageURLs();
}
