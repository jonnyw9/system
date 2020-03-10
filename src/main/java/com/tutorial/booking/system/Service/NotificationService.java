package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.Repository.NotificationRepository;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;


    public void saveNotification(Notification notification){
        notificationRepository.save(notification);
    }

    public void eventAddedCreator(Event event){
        Notification notification = new Notification();

        String description = "Your event '"+ event.getTitle() + "' at: " +
                timeParse(event.getEventStart().toLocalDateTime().toString()) + " has been successfully added";

        notification.setUserId(event.getCreatorUserId());
        notification.setTitle("Event Added");
        notification.setDescription(description);
        notification.setActionLink("/event/view/" + event.getEventId());
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        saveNotification(notification);
    }

    public void eventAddedRecipient(Event event){
        Notification notification = new Notification();

        String description = event.getCreatorUserId().getFirstName() + " " + event.getCreatorUserId().getLastName() +
                " requested an event with you on: " + timeParse(event.getEventStart().toLocalDateTime().toString()) +
                ". Please follow the link to accept or decline the event.";

        notification.setUserId(event.getRecipientUserId());
        notification.setTitle("Event Requested");
        notification.setDescription(description);
        notification.setActionLink("/event/view/" + event.getEventId());
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        saveNotification(notification);
    }

    public void eventCancelled(Event event){
        Notification notification = new Notification();

        String title = "Event Cancelled";
        notification.setTitle(title);
        String description = "Your event '"+ event.getTitle() + "' at: " +
                timeParse(event.getEventStart().toLocalDateTime().toString()) + " has been cancelled.";
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        notification.setDescription(description);

        if(event.getCreatorUserId().getUserId() == event.getRecipientUserId().getUserId()){
            User user = userService.getUserById(event.getCreatorUserId().getUserId());
            notification.setUserId(user);
            notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            saveNotification(notification);
        }else{
            User user = userService.getUserById(event.getCreatorUserId().getUserId());
            notification.setUserId(user);
            notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            saveNotification(notification);

            Notification notification1 = new Notification();
            User user1 = userService.getUserById(event.getRecipientUserId().getUserId());
            notification1.setTitle(title);
            notification1.setDescription(description);
            notification1.setUserId(user1);
            notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            saveNotification(notification1);
        }
    }

    public void eventUpdated(Event event){
        Notification notification = new Notification();
        notification.setTitle("Event Details Updated");


        String description = "Your event on " + timeParse(event.getEventStart().toLocalDateTime().toString()) + ", has been updated";
        notification.setDescription(description);
        notification.setActionLink("/event/view/" + event.getEventId());
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        if(event.getCreatorUserId().getUserId() == event.getRecipientUserId().getUserId()){
            notification.setUserId(event.getCreatorUserId());
            saveNotification(notification);
        }else{
            notification.setUserId(event.getCreatorUserId());
            saveNotification(notification);

            Notification notification1 = new Notification();
            notification1.setTitle(notification.getTitle());
            notification1.setDescription(notification.getDescription());
            notification1.setActionLink(notification.getActionLink());
            notification1.setCreatedOn(notification.getCreatedOn());

            notification1.setUserId(event.getRecipientUserId());
            saveNotification(notification1);
        }
    }

    public void userDetailsChanged(User user, User before){

    }

    public void eventTitleChanged(Event event){

    }

    public void eventDescriptionChanged(Event event){

    }

    public void eventLocationChanged(Event event){
        
    }

    public List<Notification> getUserNotifications(int id){
        return notificationRepository.findAllByUserIdOrderByCreatedOnDesc(userService.getUserById(id));
    }

    public Notification getNotificationById(int id){
        return notificationRepository.getOne(id);
    }

    public String timeParse(String time){
        return time.replace("T", " ");
    }
}
