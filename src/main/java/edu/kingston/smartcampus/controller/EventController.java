package edu.kingston.smartcampus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.kingston.smartcampus.dto.EventCreateDto;
import edu.kingston.smartcampus.dto.EventDto;
import edu.kingston.smartcampus.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/api/events")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventCreateDto dto) {
        EventDto eventDto = eventService.createEvent(dto);
        return ResponseEntity.ok(eventDto);
    }

    @GetMapping("/api/events/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        EventDto eventDto = eventService.getEventById(id);
        return ResponseEntity.ok(eventDto);
    }
}
