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

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(Notification notification, User user){
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(user.getEmail());

        msg.setSubject(notification.getTitle());

        String body = notification.getDescription();

        if(notification.getActionLink() != null){
            if(!notification.getActionLink().isEmpty()){
                body += " Click here to view: https://jwbookingsystem.herokuapp.com" + notification.getActionLink();
            }
        }

        msg.setText(body);

        javaMailSender.send(msg);
    }
}
