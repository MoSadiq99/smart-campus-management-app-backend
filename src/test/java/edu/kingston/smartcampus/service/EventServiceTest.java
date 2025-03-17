package edu.kingston.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.kingston.smartcampus.dto.EventCreateDto;
import edu.kingston.smartcampus.dto.EventDto;
import edu.kingston.smartcampus.model.Event;
import edu.kingston.smartcampus.model.enums.EventStatus;
import edu.kingston.smartcampus.model.user.Admin;
import edu.kingston.smartcampus.repository.EventRepository;
import edu.kingston.smartcampus.repository.UserRepository;

public class EventServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private EventCreateDto eventCreateDto;
    private Admin admin;

    private Event existingEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Admin();
        admin.setId(1L);

        eventCreateDto = new EventCreateDto();
        eventCreateDto.setTitle("Test Event");
        eventCreateDto.setOrganizerId(admin.getId());
        eventCreateDto.setDescription("Test Description");
        eventCreateDto.setStartTime(LocalDateTime.of(2025, 3, 17, 10, 0));
        eventCreateDto.setEndTime(LocalDateTime.of(2025, 3, 17, 12, 0));
        eventCreateDto.setLocation("Test Location");
        eventCreateDto.setCapacity(100);
        eventCreateDto.setStatus(EventStatus.UPCOMING);

        existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setTitle("Old Event");
        existingEvent.setOrganizer(admin);
        existingEvent.setDescription("Old Description");
        existingEvent.setStartTime(LocalDateTime.of(2025, 3, 17, 9, 0));
        existingEvent.setEndTime(LocalDateTime.of(2025, 3, 17, 11, 0));
        existingEvent.setLocation("Old Location");
        existingEvent.setCapacity(50);
        existingEvent.setStatus(EventStatus.UPCOMING);
    }

    @Test
    void testCreateEvent() {
        when(userRepository.getReferenceById(admin.getId())).thenReturn(admin);
        Event event = new Event();
        event.setTitle(eventCreateDto.getTitle());
        event.setOrganizer(admin);
        event.setDescription(eventCreateDto.getDescription());
        event.setStartTime(eventCreateDto.getStartTime());
        event.setEndTime(eventCreateDto.getEndTime());
        event.setLocation(eventCreateDto.getLocation());
        event.setCapacity(eventCreateDto.getCapacity());
        event.setStatus(eventCreateDto.getStatus());

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDto eventDto = eventService.createEvent(eventCreateDto);

        assertNotNull(eventDto);
        assertEquals(eventCreateDto.getTitle(), eventDto.getTitle());
        assertEquals(eventCreateDto.getDescription(), eventDto.getDescription());
        assertEquals(eventCreateDto.getLocation(), eventDto.getLocation());
        assertEquals(eventCreateDto.getCapacity(), eventDto.getCapacity());
        assertEquals(eventCreateDto.getStatus(), eventDto.getStatus());
        assertEquals(admin.getId(), eventDto.getOrganizerId());
    }

    @Test
    void testUpdateEvent() {
        when(userRepository.getReferenceById(admin.getId())).thenReturn(admin);
        when(eventRepository.findById(existingEvent.getId())).thenReturn(java.util.Optional.of(existingEvent));
        Event updatedEvent = new Event();
        updatedEvent.setId(existingEvent.getId());
        updatedEvent.setTitle(eventCreateDto.getTitle());
        updatedEvent.setOrganizer(admin);
        updatedEvent.setDescription(eventCreateDto.getDescription());
        updatedEvent.setStartTime(eventCreateDto.getStartTime());
        updatedEvent.setEndTime(eventCreateDto.getEndTime());
        updatedEvent.setLocation(eventCreateDto.getLocation());
        updatedEvent.setCapacity(eventCreateDto.getCapacity());
        updatedEvent.setStatus(eventCreateDto.getStatus());

        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        EventDto eventDto = eventService.updateEvent(existingEvent.getId(), eventCreateDto);

        assertNotNull(eventDto);
        assertEquals(eventCreateDto.getTitle(), eventDto.getTitle());
        assertEquals(eventCreateDto.getDescription(), eventDto.getDescription());
        assertEquals(eventCreateDto.getLocation(), eventDto.getLocation());
        assertEquals(eventCreateDto.getCapacity(), eventDto.getCapacity());
        assertEquals(eventCreateDto.getStatus(), eventDto.getStatus());
        assertEquals(admin.getId(), eventDto.getOrganizerId());
    }

    @Test
    void testGetEventById() {
        when(eventRepository.findById(existingEvent.getId())).thenReturn(Optional.of(existingEvent));

        EventDto eventDto = eventService.getEventById(existingEvent.getId());

        assertNotNull(eventDto);
        assertEquals(existingEvent.getId(), eventDto.getEventId());
        assertEquals(existingEvent.getTitle(), eventDto.getTitle());
        assertEquals(existingEvent.getDescription(), eventDto.getDescription());
        assertEquals(existingEvent.getLocation(), eventDto.getLocation());
        assertEquals(existingEvent.getCapacity(), eventDto.getCapacity());
        assertEquals(existingEvent.getStatus(), eventDto.getStatus());
        assertEquals(existingEvent.getOrganizer().getId(), eventDto.getOrganizerId());
    }
}
