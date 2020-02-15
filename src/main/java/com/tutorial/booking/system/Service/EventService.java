package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.dao.EventDao;
import com.tutorial.booking.system.dao.UserDao;
import com.tutorial.booking.system.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    public void add(EventDao eventDao) throws ParseException {

        Timestamp eventStart = Timestamp.valueOf((eventDao.getEventStart() + ":00").replace("T", " "));

        Timestamp eventEnd = Timestamp.valueOf((eventDao.getEventEnd() + ":00").replace("T", " "));

        Event event = new Event(eventDao.getEventId(), eventStart, eventEnd, eventDao.getTitle(),
                eventDao.getDescription(), eventDao.getCreatorUserId(), eventDao.getRecipientUserId());

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

    public void updateEvent(EventDao eventDao){

        System.out.println(eventDao.toString());

        Timestamp eventStart = Timestamp.valueOf((eventDao.getEventStart() + ":00").replace("T", " "));

        Timestamp eventEnd = Timestamp.valueOf((eventDao.getEventEnd() + ":00").replace("T", " "));

        Event event = getByEventId(eventDao.getEventId());

        System.out.println(event.toString());


        event.setDescription(eventDao.getDescription());
        event.setEventEnd(eventEnd);
        event.setEventStart(eventStart);
        event.setTitle(eventDao.getTitle());

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

}
