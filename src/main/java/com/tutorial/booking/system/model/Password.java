package com.tutorial.booking.system.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "password")
public class Password implements Serializable {

    //For a bug
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int passwordId;

    private String password;

    public Password() {
    }

    public Password(int passwordId, String password){
        this.passwordId = passwordId;
        this.password = password;
    }

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
