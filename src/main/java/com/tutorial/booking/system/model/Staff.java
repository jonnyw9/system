package com.tutorial.booking.system.model;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;

    private String room;

    public Staff() {
    }

    public Staff(int staffId, String room) {
        this.staffId = staffId;
        this.room = room;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
