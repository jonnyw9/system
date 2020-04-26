/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p>Entity class for the Notification table. The Notification table will map to this class.</p>
 * @author Jonathan Watt
 */
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User userId;

    private String title;
    private String description;
    private String actionLink;
    private boolean seen;
    private Timestamp createdOn;

    public Notification() {
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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

    public String getActionLink() {
        return actionLink;
    }

    public void setActionLink(String actionLink) {
        this.actionLink = actionLink;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", userId=" + userId.getUserId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", actionLink='" + actionLink + '\'' +
                ", seen=" + seen +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return getNotificationId() == that.getNotificationId() &&
                isSeen() == that.isSeen() &&
                Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getActionLink(), that.getActionLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotificationId(), getUserId(), getTitle(), getDescription(), getActionLink(), isSeen(), getCreatedOn());
    }
}
