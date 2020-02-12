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

}
