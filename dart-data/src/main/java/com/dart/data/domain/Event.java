package com.dart.data.domain;

import java.util.Collection;
import java.util.Date;

/**
 * Event interface that provides necessary methods
 * for performing business logic.
 *
 * @author RMPader
 */
public interface Event extends Entity {

    User getOrganizer();

    void setTitle(String title);

    String getTitle();

    void setDescription(String description);

    String getDescription();

    void addImageURL(String url);

    Collection<String> getImageURLs();

    void setStartDate(Date date);

    Date getStartDate();

    void setEndDate(Date date);

    Date getEndDate();

}
