package com.tutorial.booking.system.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creatorUserId", referencedColumnName = "userId")
    private User creatorUserId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipientUserId", referencedColumnName = "userId")
    private User recipientUserId;

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
}
