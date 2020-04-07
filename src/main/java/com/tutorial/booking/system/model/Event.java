package com.tutorial.booking.system.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.sql.Timestamp;



@Entity
@Transactional
@Table(name = "bs_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    private Timestamp eventStart;
    private Timestamp eventEnd;

    private String title;

    private String description;

    private boolean accepted;

    private String location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creatorUserId", referencedColumnName = "userId")
    private User creatorUserId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipientUserId", referencedColumnName = "userId")
    private User recipientUserId;

    public Event(){}

    public Event(int eventId, Timestamp eventStart, Timestamp eventEnd, String title, String description,
                 User creatorUserId, User recipientUserId, String location){

        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.accepted = false;
        this.creatorUserId = creatorUserId;
        this.recipientUserId = recipientUserId;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.location = location;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Timestamp getEventStart() {
        return eventStart;
    }

    public void setEventStart(Timestamp eventStart) {
        this.eventStart = eventStart;
    }

    public Timestamp getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Timestamp eventEnd) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventStart=" + eventStart +
                ", eventEnd=" + eventEnd +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", accepted=" + accepted +
                ", creatorUserId=" + creatorUserId +
                ", recipientUserId=" + recipientUserId +
                '}';
    }
}
