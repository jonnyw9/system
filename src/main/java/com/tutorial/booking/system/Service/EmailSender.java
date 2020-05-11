/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>A class to send emails for the notifications passed.</p>
 * @author Jonathan Watt
 */
@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * <p>Sends an email based on a notification passed in.</p>
     * @param notification - The notification to make the email from.
     * @param user - The user who the email is to.
     */
    public void sendMail(Notification notification, User user){
        //Creates a simplemailmessage
        SimpleMailMessage msg = new SimpleMailMessage();

        //Sets the send to email address to the email address of the user
        msg.setTo(user.getEmail());

        //Sets the subject to the notification title
        msg.setSubject(notification.getTitle());
        //Adds the description to the body
        String body = notification.getDescription();

        //If there is a link in the notification
        if(notification.getActionLink() != null){
            if(!notification.getActionLink().isEmpty()){
                //Create a link the user can click to see the notification
                body += " Click here to view: https://www.jwbooking.tech" + notification.getActionLink();
            }
        }
        //Set the body of the email
        msg.setText(body);
        //Send the email
        javaMailSender.send(msg);
    }
}
