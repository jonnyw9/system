/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Calendar;
import com.tutorial.booking.system.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Override
    public Event add(EventDto eventDto, UserDto userDto){
        //Convert the string time from the eventDto to the required Timestamp
        ArrayList<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        //Created the event object
        Event event = new Event(eventDto.getEventId(), timestamps.get(0), timestamps.get(1), eventDto.getTitle(),
                eventDto.getDescription(), eventDto.getCreatorUserId(), eventDto.getRecipientUserId(),
                eventDto.getLocation());

        //If the user is booking themselves automatically accept the event.
        if(userDto.getUserId() == event.getRecipientUserId().getUserId()){
            event.setAccepted(true);
        }

        //Return the saved event.
        return eventRepository.save(event);
    }
    @Override
    public List<Event> addRecurring(EventDto eventDto, UserDto userDto){
        //Create a list of the events that will be made.
        List<Event> events = new ArrayList<>();
        //For the specified recurring length
        for (int i = 0; i < eventDto.getRecurringLength(); i++) {
            //Add the event
            Event event = add(eventDto, userDto);
            events.add(event);
            //Move the next event to be added forward by one week.
            LocalDateTime start = LocalDateTime.parse(eventDto.getEventStart());
            LocalDateTime end = LocalDateTime.parse(eventDto.getEventEnd());

            start = start.plusDays(7);
            end = end.plusDays(7);

            eventDto.setEventStart(start.toString());
            eventDto.setEventEnd(end.toString());
        }
        return events;
    }
    @Override
    public List<Event> getEventsForUser(int userId){

        //Get List of events that the user has created.
        List<Event> events = eventRepository.findByCreatorUserId(userService.getUserById(userId));
        //Get List of events that the user has received.
        List<Event> eventsReceived = eventRepository.findByRecipientUserId(userService.getUserById(userId));
        //If the received List not is empty
        if(!eventsReceived.isEmpty()){
            //For look through the received events
            for(int i = 0; i < eventsReceived.size(); i++){
                //If the created events not is empty
                if(!events.isEmpty()){
                    boolean addable = true;
                    //Go through the created events
                    for (int j = 0; j < events.size() ; j++) {
                        //If the recieved event is the same as the created one
                        if(events.get(j).getEventId() == eventsReceived.get(i).getEventId()){
                            //Make sure it isn't addable
                            addable = false;
                            break;
                        }
                    }
                    //If if is addable add it
                    if(addable){
                        events.add(eventsReceived.get(i));
                    }
                }else{
                    //If there is no created events return the received events
                    return eventsReceived;
                }
            }
        }
        //This will return a List of events
        return events;
    }
    @Override
    public Event getByEventId(int id){
        //Get the Optional of that event from the database
        Optional<Event> optionalEvent = eventRepository.findEventByEventId(id);
        //Return the event, or if it's not there. Return null
        return optionalEvent.orElse(null);
    }

    @Override
    public String changeTimestampToString(Timestamp time){
        try {
            //Remove the parts of the string that it aren't needed for the conversion
            String returnTime = time.toString().substring(0,16);
            //Replace the space with the T
            returnTime = returnTime.replace(' ', 'T');
            //Return that string
            return returnTime;
        }catch (Exception e){
            //If it fails return a generic time
            return "0000-01-01T00:00";
        }

    }
    @Override
    public Event updateEvent(EventDto eventDto){

        //Get the event by the id
        Event event = getByEventId(eventDto.getEventId());

        //Update the details
        event.setDescription(eventDto.getDescription());
        event.setTitle(eventDto.getTitle());
        event.setLocation(eventDto.getLocation());

        //Save and return the event
        return eventRepository.save(event);
    }
    @Override
    public void cancelEvent(int eventId){
        //Get the event to cancelled
        Event event = getByEventId(eventId);
        //Send a notification of cancellation
        notificationService.eventCancelled(event);
        //Can't delete with these columns containing data so have to nullify them
        event.setCreatorUserId(null);
        event.setRecipientUserId(null);
        eventRepository.save(event);
        //Finally Delete
        eventRepository.delete(event);
    }
    @Override
    public ArrayList<Timestamp> convertStringToTimeStamp(EventDto eventDto){
        //Declare the ArrayList to be returned
        ArrayList<Timestamp> returnTimeStamp = new ArrayList<>();
        //Parse the string of the event start timestamp into a Timestamp
        Timestamp eventStart = Timestamp.valueOf((eventDto.getEventStart() + ":00").replace("T", " "));
        //Parse the string of the event end timestamp into a Timestamp
        Timestamp eventEnd = Timestamp.valueOf((eventDto.getEventEnd() + ":00").replace("T", " "));
        //Add the timestamps to the ArrayLists
        returnTimeStamp.add(eventStart);
        returnTimeStamp.add(eventEnd);
        //return the ArrayList
        return returnTimeStamp;
    }
    @Override
    public ArrayList<Timestamp> calulateFreeTimeForTheWeek(Calendar calendar){

        LocalDateTime localDateTime = LocalDateTime.now();

        LocalDateTime start = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
                localDateTime.getDayOfMonth(), calendar.getDayStartTime().toLocalTime().getHour(),
                calendar.getDayStartTime().toLocalTime().getMinute());

        LocalDateTime end = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
                localDateTime.getDayOfMonth(), calendar.getDayEndTime().toLocalTime().getHour(),
                calendar.getDayEndTime().toLocalTime().getMinute());

        LocalDate day = localDateTime.toLocalDate();
        final long plusDay = 1;
        final long plusMinutes = 30;
        ArrayList<Timestamp> availableEvents = new ArrayList<>();

        //TODO Maybe use string here instead of value
        for(int i = 0; i <= 4; i++){

            LocalDateTime current = start;

            System.out.println(current.toString());

            //Get Events for that day
            Timestamp currentTimeStamp = Timestamp.valueOf(start);
            Timestamp currentTimeStampEnd = Timestamp.valueOf(end);
            List<Event> eventsForDay = eventRepository.findEventByEventStartBeforeAndEventStartAfter(
                    currentTimeStampEnd, currentTimeStamp);

            if(current.toLocalDate().isBefore(LocalDate.now().plusDays(1))){
                i--;
                end = end.plusDays(plusDay);
                start = start.plusDays(plusDay);
                day = day.plusDays(plusDay);
                continue;
            }
            if(day.getDayOfWeek().getValue() == 6 || day.getDayOfWeek().getValue() == 7){
                i--;
                end = end.plusDays(plusDay);
                start = start.plusDays(plusDay);
                day = day.plusDays(plusDay);
                continue;
            }

            while(!current.equals(end)){

                if(LocalDate.now().isBefore(ChronoLocalDate.from(end))){
                    if(!eventsForDay.isEmpty()){
                        //For loop of all the events for that day
                        for(Event event : eventsForDay){
                            //Compare event times
                            //If they dont equal
                            if(!event.getEventStart().toLocalDateTime().isEqual(current)){
                                //Add the time to the list
                                Timestamp freeTime = Timestamp.valueOf(current);
                                System.out.println(freeTime.toString());
                                availableEvents.add(freeTime);
                                break;
                            }
                        }
                    }else{
                        Timestamp freeTime = Timestamp.valueOf(current);
                        System.out.println(freeTime.toString());
                        availableEvents.add(freeTime);
                    }
                    //Add half an hour to current
                    current = current.plusMinutes(plusMinutes);
                }else{
                    break;
                }


            }
            end = end.plusDays(plusDay);
            start = start.plusDays(plusDay);
            day = day.plusDays(plusDay);
        }

        return availableEvents;
    }
    @Override
    public void acceptEvent(int id){
        //Get the event to be accepted
        Event event = eventRepository.getOne(id);
        //Set it to accepted
        event.setAccepted(true);
        //Save it
        eventRepository.save(event);
        //Send the notification
        notificationService.eventAccepted(event);
    }
    @Override
    public boolean listEventsNearTimeEventUsers(EventDto eventDto){
        //Get timestamps of the event.
        List<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        //Get events for the creator and the recipient and add them to the same list.
        List<Event> eventsCreator = getEventsForUser(eventDto.getCreatorUserId().getUserId());
        List<Event> eventsRecipient = getEventsForUser(eventDto.getRecipientUserId().getUserId());

        List<Event> events = new ArrayList<>();

        events.addAll(eventsCreator);
        events.addAll(eventsRecipient);

        //Loop through the list of all the events to see which ones overlap
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            LocalDateTime startA = event.getEventStart().toLocalDateTime();
            LocalDateTime endA = event.getEventEnd().toLocalDateTime();

            LocalDateTime startB = timestamps.get(0).toLocalDateTime();
            LocalDateTime endB = timestamps.get(1).toLocalDateTime();
            if(startA.isBefore(endB) && startB.isBefore(endA)){
                //If they overlap return true
                return true;
            }
        }
        //Return false because there are no conflicts
        return false;
    }
    @Override
    @Scheduled(fixedRate = 120000)
    public void CheckUpcomingEvents(){
        //Check events that are in 30 mins
        Timestamp timestamp1 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(29));
        Timestamp timestamp2 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(31));
        List<Event> events = eventRepository.findAllByEventStartInXMins(timestamp1, timestamp2);
        notifyUsersOfUpcomingEvents(events, "30 Minutes");

        //Check events that are in a hour
        timestamp1 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(58));
        timestamp2 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(62));
        events = eventRepository.findAllByEventStartInXMins(timestamp1, timestamp2);
        notifyUsersOfUpcomingEvents(events, "1 Hour");
    }
    @Override
    public void notifyUsersOfUpcomingEvents(List<Event> events, String time){
        //Notifies everyone for all the events
        for(Event event: events){
            notificationService.notifyUpcomingEvent(event, time);
        }
    }
}
