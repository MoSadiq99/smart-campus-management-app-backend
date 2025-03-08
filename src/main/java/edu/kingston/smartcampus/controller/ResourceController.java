package edu.kingston.smartcampus.controller;

import edu.kingston.smartcampus.dto.ReservationCreateDto;
import edu.kingston.smartcampus.dto.ReservationDto;
import edu.kingston.smartcampus.model.Reservation;
import edu.kingston.smartcampus.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.kingston.smartcampus.dto.ResourceCreateDto;
import edu.kingston.smartcampus.dto.ResourceDto;
import edu.kingston.smartcampus.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final ReservationService reservationService;

    // POST a new resource
    @PostMapping
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceCreateDto dto) {
        ResourceDto resourceDto = resourceService.createResource(dto);
        return ResponseEntity.ok(resourceDto);
    }

    // GET resource by id
    @GetMapping("/{id}")
    public ResponseEntity<ResourceDto> getResourceById(@PathVariable Long id) {
        ResourceDto resourceDto = resourceService.getResourceById(id);
        return ResponseEntity.ok(resourceDto);
    }

    // PUT to update an existing resource
    @PutMapping("/{id}")
    public ResponseEntity<ResourceDto> updateResource(@PathVariable Long id, @Valid @RequestBody ResourceCreateDto dto) {
        ResourceDto resourceDto = resourceService.updateResource(id, dto);
        return ResponseEntity.ok(resourceDto);
    }

    // DELETE a resource
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok().build();
    }

    // GET all resources
    @GetMapping
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        List<ResourceDto> resources = resourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    // GET reservations for a specific time range
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> getReservations(
            @RequestParam("from") String from,
            @RequestParam("to") String to) {
        LocalDateTime start = LocalDateTime.parse(from);
        LocalDateTime end = LocalDateTime.parse(to);
        List<Reservation> reservations = reservationService.getReservationsInRange(start, end);
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservationService::mapToReservationDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDtos);
    }

    // POST a new reservation
    @PostMapping("/reservations")
    public ResponseEntity<ReservationDto> createReservation(
            @RequestBody ReservationCreateDto reservationCreateDto,
            @RequestParam("userId") Long userId) {
        ReservationDto reservationDto = reservationService.createReservation(reservationCreateDto, userId);
        return ResponseEntity.ok(reservationDto);
    }

    // PUT to update an existing reservation
    @PutMapping("/reservations/{id}")
    public ResponseEntity<Void> updateReservation(
            @PathVariable("id") Long id,
            @RequestBody ReservationCreateDto reservationCreateDto) {
        reservationService.updateReservation(id, reservationCreateDto);
        return ResponseEntity.ok().build();
    }

    // DELETE a reservation
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
