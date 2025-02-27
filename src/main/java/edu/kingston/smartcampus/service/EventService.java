package edu.kingston.smartcampus.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import edu.kingston.smartcampus.model.Event;
import edu.kingston.smartcampus.repository.EventRepository;

import jakarta.transaction.Transactional;

@Service
public class EventService {
    
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
