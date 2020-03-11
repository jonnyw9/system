package com.tutorial.booking.system.dto;

import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class EventDto {

    private int eventId;

    private String eventStart;
    private String eventEnd;

    private Timestamp eventStartTimeStamp;

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String description;

    private boolean accepted;

    private User creatorUserId;

    private User recipientUserId;

    @NotNull
    @NotEmpty
    private String location;

    @Min(2)
    private Integer recurringLength;


    public EventDto() {
    }

    public EventDto(int eventId, String eventStart, String eventEnd, String title, String description, boolean accepted,
                    User creatorUserId, User recipientUserId, String location) {
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
        this.location = location;
    }

    public EventDto(Event event){
        this.eventId = event.getEventId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.accepted = event.isAccepted();
        this.creatorUserId = event.getCreatorUserId();
        this.recipientUserId = event.getRecipientUserId();
        this.location = event.getLocation();
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

    public Timestamp getEventStartTimeStamp() {
        return eventStartTimeStamp;
    }

    public void setEventStartTimeStamp(Timestamp eventStartTimeStamp) {
        this.eventStartTimeStamp = eventStartTimeStamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRecurringLength() {
        return recurringLength;
    }

    public void setRecurringLength(Integer recurringLength) {
        this.recurringLength = recurringLength;
    }


    @Override
    public String toString() {
        return "EventDto{" +
                "eventId=" + eventId +
                ", eventStart='" + eventStart + '\'' +
                ", eventEnd='" + eventEnd + '\'' +
                ", eventStartTimeStamp=" + eventStartTimeStamp +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", accepted=" + accepted +
                ", creatorUserId=" + creatorUserId +
                ", recipientUserId=" + recipientUserId +
                '}';
    }
}
