package com.tutorial.booking.system.model;

import javax.persistence.*;

@Entity
@Table(name = "bs_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eventId;

    private String eventStart;
    private String eventEnd;

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
}
