package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.Timestamp;
import java.text.ParseException;
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

}
