package com.dart.data.domain;

import java.util.Collection;

/**
 * Post interface that provides necessary methods
 * for performing business logic.
 *
 * @author RMPader
 */
public interface Post extends Entity {

    Event getEvent();

    User getUser();

    void setContent(String content);

    String getContent();

    void addImageURL(String url);

    Collection<String> getImageURLs();
}
