package com.tutorial.booking.system.dao;

import com.tutorial.booking.system.model.Event;
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

    public EventDao() {
    }

    public EventDao(int eventId, String eventStart, String eventEnd, String title, String description, boolean accepted,
                    User creatorUserId, User recipientUserId) {
        this.eventId = eventId;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.title = title;
        this.description = description;
        this.accepted = accepted;
        this.creatorUserId = creatorUserId;
        this.recipientUserId = recipientUserId;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    public EventDao (Event event){
        this.eventId = event.getEventId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.accepted = event.isAccepted();
        this.creatorUserId = event.getCreatorUserId();
        this.recipientUserId = event.getRecipientUserId();

    }

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
