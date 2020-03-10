package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Calendar;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    public void add(EventDto eventDto, UserDto userDto) throws ParseException {

        ArrayList<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        Event event = new Event(eventDto.getEventId(), timestamps.get(0), timestamps.get(1), eventDto.getTitle(),
                eventDto.getDescription(), eventDto.getCreatorUserId(), eventDto.getRecipientUserId(),
                eventDto.getLocation());

        if(userDto.getUserId() == event.getRecipientUserId().getUserId()){
            event.setAccepted(true);
        }

        this.eventRepository.save(event);

        notificationService.eventAddedCreator(event);
        if(event.getCreatorUserId().getUserId() != event.getCreatorUserId().getUserId()){
            notificationService.eventAddedRecipient(event);
        }
    }

    public void addRecurring(EventDto eventDto, UserDto userDto) throws ParseException{
        for (int i = 0; i < eventDto.getRecurringLength(); i++) {
            this.add(eventDto, userDto);
            LocalDateTime start = LocalDateTime.parse(eventDto.getEventStart());
            LocalDateTime end = LocalDateTime.parse(eventDto.getEventEnd());

            start = start.plusDays(7);
            end = end.plusDays(7);

            eventDto.setEventStart(start.toString());
            eventDto.setEventEnd(end.toString());
        }
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


        System.out.println(event.toString());

        ArrayList<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        event.setDescription(eventDto.getDescription());
        event.setEventEnd(timestamps.get(1));
        event.setEventStart(timestamps.get(0));
        event.setTitle(eventDto.getTitle());
        event.setLocation(eventDto.getLocation());

        eventRepository.save(event);

        notificationService.eventUpdated(event);
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
    }
}
