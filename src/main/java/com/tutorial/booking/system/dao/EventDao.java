package com.tutorial.booking.system.dao;

import com.tutorial.booking.system.model.User;

public class EventDao {

    private int eventId;

    private String eventStart;
    private String eventEnd;

    private String title;

    private String description;

    private boolean accepted;

    private User creatorUserId;
    private User recipientUserId;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public User getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(User creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public User getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(User recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    @Override
    public String toString() {
        return "EventDao{" +
                "eventId=" + eventId +
                ", eventStart='" + eventStart + '\'' +
                ", eventEnd='" + eventEnd + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", accepted=" + accepted +
                ", creatorUserId=" + creatorUserId +
                ", recipientUserId=" + recipientUserId +
                '}';
    }
}
