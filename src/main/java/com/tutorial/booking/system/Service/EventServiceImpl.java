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
public class EventServiceImpl {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    NotificationServiceImpl notificationService;

    public void add(EventDto eventDto, UserDto userDto, Boolean recurring) throws ParseException {

        ArrayList<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        Event event = new Event(eventDto.getEventId(), timestamps.get(0), timestamps.get(1), eventDto.getTitle(),
                eventDto.getDescription(), eventDto.getCreatorUserId(), eventDto.getRecipientUserId(),
                eventDto.getLocation());

        if(userDto.getUserId() == event.getRecipientUserId().getUserId()){
            event.setAccepted(true);
        }

        this.eventRepository.save(event);

        if(!recurring){
            notificationService.eventAddedCreator(event);
        }

        if(event.getCreatorUserId().getUserId() != event.getRecipientUserId().getUserId()){
            notificationService.eventAddedRecipient(event);
        }
    }

    public void addRecurring(EventDto eventDto, UserDto userDto) throws ParseException{
        for (int i = 0; i < eventDto.getRecurringLength(); i++) {
            this.add(eventDto, userDto, true);
            LocalDateTime start = LocalDateTime.parse(eventDto.getEventStart());
            LocalDateTime end = LocalDateTime.parse(eventDto.getEventEnd());

            start = start.plusDays(7);
            end = end.plusDays(7);

            eventDto.setEventStart(start.toString());
            eventDto.setEventEnd(end.toString());
        }
        notificationService.eventRecurringAdded(userService.getUserById(userDto.getUserId()));
    }

    public List<Event> getEventsForUser(int userId){

        //Get List
        List<Event> events = eventRepository.findByCreatorUserId(userService.getUserById(userId));

        for (int i = 0; i < events.size() ; i++) {
            System.out.println("CREATED:   " + events.get(i).toString());
        }



        List<Event> eventsReceived = eventRepository.findByRecipientUserId(userService.getUserById(userId));

        for (int i = 0; i < eventsReceived.size() ; i++) {
            System.out.println(eventsReceived.get(i).toString());
        }



        if(!eventsReceived.isEmpty()){
            for(int i = 0; i < eventsReceived.size(); i++){
                if(!events.isEmpty()){
                    boolean addable = true;
                    for (int j = 0; j < events.size() ; j++) {
                        if(events.get(j).getEventId() == eventsReceived.get(i).getEventId()){
                            System.out.println(events.get(j).getEventId() + " " + eventsReceived.get(i).getEventId());
                            addable = false;
                        }
                    }
                    if(addable){
                        events.add(eventsReceived.get(i));
                    }
                }else{
                    return eventsReceived;
                }

            }
        }

        for (int i = 0; i < events.size(); i++) {
            System.out.println("FULL LIST:   " + events.get(i).toString());
        }
       // events.

        //This will return a List of events
        return events;
    }

    public Event getByEventId(int id){

        Optional<Event> optionalEvent = eventRepository.findEventByEventId(id);

        return optionalEvent.orElse(null);
    }

    public String changeTimestapToString(Timestamp time){
        String returnTime = time.toString().substring(0,16);

        returnTime = returnTime.replace(' ', 'T');

        System.out.println(returnTime);

        return returnTime;
    }

    public void updateEvent(EventDto eventDto){

        System.out.println(eventDto.toString());

        Event event = getByEventId(eventDto.getEventId());

        String locationBefore = event.getLocation();

        System.out.println(event.toString());

        ArrayList<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        event.setDescription(eventDto.getDescription());
        event.setEventEnd(timestamps.get(1));
        event.setEventStart(timestamps.get(0));
        event.setTitle(eventDto.getTitle());
        event.setLocation(eventDto.getLocation());

        eventRepository.save(event);

        notificationService.eventUpdated(event, locationBefore);
    }

    public void cancelEvent(int eventId){

        Event event = getByEventId(eventId);
        notificationService.eventCancelled(event);
        //Can't delete with these columns containing data so have to nullify them
        event.setCreatorUserId(null);
        event.setRecipientUserId(null);
        eventRepository.save(event);

        eventRepository.delete(event);


    }

    public ArrayList<Timestamp> convertStringToTimeStamp(EventDto eventDto){
        ArrayList<Timestamp> returnTimeStamp = new ArrayList<>();

        Timestamp eventStart = Timestamp.valueOf((eventDto.getEventStart() + ":00").replace("T", " "));

        Timestamp eventEnd = Timestamp.valueOf((eventDto.getEventEnd() + ":00").replace("T", " "));

        returnTimeStamp.add(eventStart);
        returnTimeStamp.add(eventEnd);

        return returnTimeStamp;
    }

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

    public void acceptEvent(int id){
        Event event = eventRepository.getOne(id);

        event.setAccepted(true);

        eventRepository.save(event);

        notificationService.eventAccepted(event);
    }

    public List<Event> listEventsNearTimeEventUsers(EventDto eventDto){
        List<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        List<Event> eventsCreator = getEventsForUser(eventDto.getCreatorUserId().getUserId());
        List<Event> eventsRecipient = getEventsForUser(eventDto.getRecipientUserId().getUserId());

        List<Event> events = new ArrayList<>();

        events.addAll(eventsCreator);
        events.addAll(eventsRecipient);

        List<Event> eventsReturned = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            LocalDateTime startA = event.getEventStart().toLocalDateTime();
            LocalDateTime endA = event.getEventEnd().toLocalDateTime();

            LocalDateTime startB = timestamps.get(0).toLocalDateTime();
            LocalDateTime endB = timestamps.get(1).toLocalDateTime();
            if(startA.isBefore(endB) && startB.isBefore(endA)){
                eventsReturned.add(event);
            }
        }

        return eventsReturned;
    }

    @Scheduled(fixedRate = 150000)
    public void checkEvents30Mins(){
        //Check events that are in 30 mins
        Timestamp timestamp1 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(28));
        Timestamp timestamp2 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(32));
        List<Event> events = eventRepository.findAllByEventStartInXMins(timestamp1, timestamp2);
        notifyUsers(events);

        //Check events that are in a hour
        timestamp1 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(58));
        timestamp2 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(62));
        events = eventRepository.findAllByEventStartInXMins(timestamp1, timestamp2);
        notifyUsers(events);
    }

    public void notifyUsers(List<Event> events){
        for(Event event: events){
            notificationService.eventIn30Minutes(event);
        }
    }
}
