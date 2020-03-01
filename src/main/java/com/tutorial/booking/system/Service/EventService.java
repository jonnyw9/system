package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.dto.EventDto;
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

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    public void add(EventDto eventDto) throws ParseException {

        ArrayList<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        Event event = new Event(eventDto.getEventId(), timestamps.get(0), timestamps.get(1), eventDto.getTitle(),
                eventDto.getDescription(), eventDto.getCreatorUserId(), eventDto.getRecipientUserId());

        this.eventRepository.save(event);
    }

    public List<Event> getEventsForUser(int userId){

        //Get List
        List<Event> events = eventRepository.findByCreatorUserId(userService.getUserById(userId));

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


        /**
         * Made this a function
        Timestamp eventStart = Timestamp.valueOf((eventDto.getEventStart() + ":00").replace("T", " "));

        Timestamp eventEnd = Timestamp.valueOf((eventDto.getEventEnd() + ":00").replace("T", " "));

         **/
        Event event = getByEventId(eventDto.getEventId());

        System.out.println(event.toString());

        ArrayList<Timestamp> timestamps = convertStringToTimeStamp(eventDto);

        event.setDescription(eventDto.getDescription());
        event.setEventEnd(timestamps.get(1));
        event.setEventStart(timestamps.get(0));
        event.setTitle(eventDto.getTitle());

        //Update to the database
        //eventRepository.updateEvent(eventDao.getEventId(), eventDao.getTitle(), eventDao.getDescription(), eventStart,
            //eventEnd);
        eventRepository.save(event);
    }

    public void cancelEvent(int eventId){

        Event event = getByEventId(eventId);


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

}
