package com.dart.event.api;

import java.util.Date;
import java.util.Objects;

/**
 * Created by RMPader on 7/28/15.
 */
public class FindEventResponse {

    private String id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String[] imageURLs;
    private Location location;
    private Identity organizer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String[] getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(String[] imageURLs) {
        this.imageURLs = imageURLs;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Identity getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Identity organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindEventResponse that = (FindEventResponse) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getStartDate(), that.getStartDate()) &&
                Objects.equals(getEndDate(), that.getEndDate()) &&
                Objects.equals(getImageURLs(), that.getImageURLs()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getOrganizer(), that.getOrganizer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getStartDate(), getEndDate(), getImageURLs(), getLocation(), getOrganizer());
    }
}
