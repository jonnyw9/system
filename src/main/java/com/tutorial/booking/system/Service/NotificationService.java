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

        String title = "Event Added";
        String description = "Your event "+ event.getTitle() + " at: " +
                event.getEventStart().toLocalDateTime().toString() + " has been successfully added";
        String actionLink = "/event/view/" + event.getEventId();

        notification.setUserId(event.getCreatorUserId());
        notification.setTitle(title);
        notification.setDescription(description);
        notification.setActionLink(actionLink);

        saveNotification(notification);
    }

    public void eventAddedRecipient(Event event){
        Notification notification = new Notification();

        String title = "Event Requested";
        String description = event.getCreatorUserId().getFirstName() + " " + event.getCreatorUserId().getLastName() +
                " requested an event with you on: " + event.getEventStart().toLocalDateTime().toString() +
                ". Please follow the link to accept or decline the event.";
        String actionLink = "/event/view/" + event.getEventId();

        notification.setUserId(event.getRecipientUserId());
        notification.setTitle(title);
        notification.setDescription(description);
        notification.setActionLink(actionLink);

        saveNotification(notification);
    }

    public void eventCancelled(Event event){
        Notification notification = new Notification();

        System.out.println(event.toString());

        String title = "Event Cancelled";
        notification.setTitle(title);
        String description = "Your event "+ event.getTitle() + " at: " +
                event.getEventStart().toLocalDateTime().toString() + " has been cancelled.";


        notification.setDescription(description);

        if(event.getCreatorUserId().getUserId() == event.getRecipientUserId().getUserId()){
            User user = userService.getUserById(event.getCreatorUserId().getUserId());
            notification.setUserId(user);
            saveNotification(notification);
        }else{
            User user = userService.getUserById(event.getCreatorUserId().getUserId());
            notification.setUserId(user);
            saveNotification(notification);

            Notification notification1 = new Notification();
            User user1 = userService.getUserById(event.getRecipientUserId().getUserId());
            notification1.setTitle(title);
            notification1.setDescription(description);
            notification1.setUserId(user1);
            saveNotification(notification1);
        }
    }

    public void eventUpdated(Event event, Event updatedEvent){

    }

    public void userDetailsChanged(User user, User before){

    }

    public List<Notification> getUserNotifications(int id){
        return notificationRepository.findAllByUserId(userService.getUserById(id));
    }

    public Notification getNotificationById(int id){
        return notificationRepository.getOne(id);
    }
}
