package edu.kingston.smartcampus.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import edu.kingston.smartcampus.dto.EventCreateDto;
import edu.kingston.smartcampus.dto.EventDto;
import edu.kingston.smartcampus.model.Event;
import edu.kingston.smartcampus.repository.EventRepository;
import edu.kingston.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EventDto createEvent(EventCreateDto dto) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setOrganizer(userRepository.getReferenceById(dto.getOrganizerId()));
        event.setDescription(dto.getDescription());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setLocation(dto.getLocation());
        event.setCapacity(dto.getCapacity());
        event.setStatus(dto.getStatus());

        Event savedEvent = eventRepository.save(event);

        EventDto eventDto = new EventDto();
        mapToEventDto(savedEvent, eventDto);
        return eventDto;
    }

    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        EventDto eventDto = new EventDto();
        mapToEventDto(event, eventDto);
        return eventDto;
    }

    public List<EventDto> getEventByTime(LocalDateTime startTime, LocalDateTime endTime) {
        List<Event> events = eventRepository.findByStartTimeBetween(startTime, endTime);
        return events.stream()
                .map(event -> {
                    EventDto eventDto = new EventDto();
                    mapToEventDto(event, eventDto);
                    return eventDto;
                })
                .collect(Collectors.toList());
    }

    private void mapToEventDto(Event event, EventDto eventDto) {
        eventDto.setEventId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setOrganizerId(event.getOrganizer().getId());
        eventDto.setDescription(event.getDescription());
        eventDto.setStartTime(event.getStartTime());
        eventDto.setEndTime(event.getEndTime());
        eventDto.setLocation(event.getLocation());
        eventDto.setCapacity(event.getCapacity());
        eventDto.setStatus(event.getStatus());
    }
}
