package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public void add(final Event event){

        this.eventRepository.save(event);
    }

}
