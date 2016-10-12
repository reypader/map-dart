package com.dart.event.api;

import java.util.Date;
import java.util.Objects;

/**
 * Created by RMPader on 7/28/15.
 */
public class CreateEventRequest {

    private String organizerId;
    private String title;
    private String description;
    private Date endDate;
    private Date startDate;
    private String[] imageURLs;
    private Location location;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateEventRequest that = (CreateEventRequest) o;
        return Objects.equals(getOrganizerId(), that.getOrganizerId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getEndDate(), that.getEndDate()) &&
                Objects.equals(getStartDate(), that.getStartDate()) &&
                Objects.equals(getImageURLs(), that.getImageURLs()) &&
                Objects.equals(getLocation(), that.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrganizerId(), getTitle(), getDescription(), getEndDate(), getStartDate(), getImageURLs(), getLocation());
    }
}
