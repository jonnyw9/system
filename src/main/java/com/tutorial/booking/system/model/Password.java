package com.tutorial.booking.system.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "password")
public class Password implements Serializable {

    //For a bug
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int passwordId;

    private String password;

    public int getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(int passwordId) {
        this.passwordId = passwordId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
