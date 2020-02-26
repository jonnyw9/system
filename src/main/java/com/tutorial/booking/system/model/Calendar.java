package com.tutorial.booking.system.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "calendar")
public class Calendar{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int calendarId;

    private Time dayStartTime;

    private Time dayEndTime;

    public Calendar() {
    }

    public Calendar(int calendarId, Time dayStartTime, Time dayEndTime) {
        this.calendarId = calendarId;
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public Time getDayStartTime() {
        return dayStartTime;
    }

    public void setDayStartTime(Time dayStartTime) {
        this.dayStartTime = dayStartTime;
    }

    public Time getDayEndTime() {
        return dayEndTime;
    }

    public void setDayEndTime(Time dayEndTime) {
        this.dayEndTime = dayEndTime;
    }
}
