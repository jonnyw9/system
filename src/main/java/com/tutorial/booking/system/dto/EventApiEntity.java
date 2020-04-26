/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.dto;

/**
 * <p>A class used during the runtime which holds elements of the Event table but which is not mapped to it.
 * It is used to send to the calendar via an API.</p>
 * @author Jonathan Watt
 */
public class EventApiEntity {

    private int id;
    private String title;
    private String start;
    private String end;

    private String url;

    public EventApiEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
