package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.Repository.NotificationRepository;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private EmailSender emailSender;


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

        emailSender.sendMail(notification, event.getCreatorUserId());
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
        emailSender.sendMail(notification, event.getRecipientUserId());
    }

    public void eventRecurringAdded(User user){
        Notification notification = new Notification();

        String description = "Your recurring events have been added. Please click the events" +
                " on your calendar for more information.";

        notification.setUserId(user);
        notification.setTitle("Events Added");
        notification.setDescription(description);
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        saveNotification(notification);
        emailSender.sendMail(notification, user);
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
            emailSender.sendMail(notification, event.getRecipientUserId());
        }else{
            User user = userService.getUserById(event.getCreatorUserId().getUserId());
            notification.setUserId(user);
            notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            saveNotification(notification);
            emailSender.sendMail(notification, user);

            Notification notification1 = new Notification();
            User user1 = userService.getUserById(event.getRecipientUserId().getUserId());
            notification1.setTitle(title);
            notification1.setDescription(description);
            notification1.setUserId(user1);
            notification1.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            saveNotification(notification1);
            emailSender.sendMail(notification, event.getRecipientUserId());
        }
    }

   public void eventUpdated(Event event, String locationBefore){

        if(!event.getLocation().equals(locationBefore)){
            eventLocationChanged(event);
        }else{
            Notification notification = new Notification();
            notification.setTitle("Event Details Updated");


            String description = "Your event on " + timeParse(event.getEventStart().toLocalDateTime().toString()) + ", has been updated";
            notification.setDescription(description);
            notification.setActionLink("/event/view/" + event.getEventId());
            notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

            if(event.getCreatorUserId().getUserId() == event.getRecipientUserId().getUserId()){
                notification.setUserId(event.getCreatorUserId());
                saveNotification(notification);
                emailSender.sendMail(notification, event.getCreatorUserId());
            }else{
                notification.setUserId(event.getCreatorUserId());
                saveNotification(notification);
                emailSender.sendMail(notification, event.getCreatorUserId());

                Notification notification1 = new Notification();
                notification1.setTitle(notification.getTitle());
                notification1.setDescription(notification.getDescription());
                notification1.setActionLink(notification.getActionLink());
                notification1.setCreatedOn(notification.getCreatedOn());

                notification1.setUserId(event.getRecipientUserId());
                saveNotification(notification1);
                emailSender.sendMail(notification1, event.getRecipientUserId());
            }
        }


    }

    public void eventLocationChanged(Event event){
        Notification notification = new Notification();
        notification.setTitle("Event Location Changed.");
        notification.setActionLink("/event/view/" + event.getEventId());
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));


        String description = "The location of the event at: "
                + timeParse(event.getEventStart().toLocalDateTime().toString()) + " has been changed to " +
                event.getLocation() + ".";

        notification.setDescription(description);
        notification.setUserId(event.getCreatorUserId());

        if(event.getRecipientUserId().getUserId() == event.getCreatorUserId().getUserId()){
            saveNotification(notification);
            emailSender.sendMail(notification, event.getCreatorUserId());
        }else{
            Notification notification1 = new Notification();
            notification1.setTitle(notification.getTitle());
            notification1.setDescription(notification.getDescription());
            notification1.setCreatedOn(notification.getCreatedOn());
            notification1.setActionLink(notification.getActionLink());
            notification1.setUserId(event.getRecipientUserId());

            saveNotification(notification);
            emailSender.sendMail(notification, event.getCreatorUserId());
            saveNotification(notification1);
            emailSender.sendMail(notification, event.getRecipientUserId());
        }
    }

    public void eventAccepted(Event event){
        Notification notification = new Notification();

        notification.setTitle("Event Accepted");
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        notification.setActionLink("/event/view/" + event.getEventId());
        notification.setUserId(event.getCreatorUserId());

        String description = "Your event with: " + event.getRecipientUserId().getFirstName() + " "
                + event.getRecipientUserId().getLastName() + ", has been accepted!";

        notification.setDescription(description);

        saveNotification(notification);
        emailSender.sendMail(notification, event.getCreatorUserId());
    }

    public void accountCreated(User user){
        Notification notification = new Notification();
        notification.setTitle("Account Created!");
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        notification.setUserId(user);

        String description = "Thank you for creating an account. Start by adding some events to you " +
                "own calendar by clicking and dragging to select the times.";

        if(user.getStaffId() == null){
            description += " Try searching for some lecturers to book. You can only book them for 30 minutes " +
                    "and 24 hours beforehand.";
        }

        notification.setDescription(description);

        saveNotification(notification);

        emailSender.sendMail(notification, user);
    }

    public void passwordChanged(User user){
        Notification notification = new Notification();

        notification.setTitle("Password Changed!");
        notification.setDescription("Your password has successfully been changed. Please login again with you new password.");

        notification.setActionLink("/home");
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        notification.setUserId(user);

        saveNotification(notification);
        emailSender.sendMail(notification, user);
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

    public void readAllUnreadNotifications(UserDto userDto){
        User user =  userService.getUserById(userDto.getUserId());

        List<Notification> notifications = notificationRepository.findAllByUserIdAndSeenIsFalse(user);

        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            notification.setSeen(true);
            saveNotification(notification);
        }
    }

    public void eventIn30Minutes(Event event){
        Notification notification = new Notification();

        notification.setTitle("Event in 30 minutes!");
        notification.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        notification.setActionLink("/event/view/" + event.getEventId());

        String description = "Your event '"+ event.getTitle() + "' at: " +
                timeParse(event.getEventStart().toLocalDateTime().toString()) + " , at location: "
                + event.getLocation() + " is in 30 minutes.";

        notification.setUserId(event.getCreatorUserId());
        notification.setDescription(description);
        saveNotification(notification);
        emailSender.sendMail(notification, event.getCreatorUserId());

        if(event.getRecipientUserId().getUserId() != event.getCreatorUserId().getUserId()){
            Notification notification1 = new Notification();
            notification1.setTitle(notification.getTitle());
            notification1.setDescription(notification.getDescription());
            notification1.setUserId(event.getRecipientUserId());
            notification1.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            notification1.setActionLink(notification.getActionLink());

            saveNotification(notification1);
            emailSender.sendMail(notification1, event.getRecipientUserId());
        }

    }
}
