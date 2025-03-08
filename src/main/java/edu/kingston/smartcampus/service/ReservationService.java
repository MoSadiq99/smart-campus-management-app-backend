package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.RecurrencePattern;
import edu.kingston.smartcampus.dto.ReservationCreateDto;
import edu.kingston.smartcampus.dto.ReservationDto;
import edu.kingston.smartcampus.model.Event;
import edu.kingston.smartcampus.model.Lecture;
import edu.kingston.smartcampus.model.Reservation;
import edu.kingston.smartcampus.model.Resource;
import edu.kingston.smartcampus.model.user.User;
import edu.kingston.smartcampus.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final LectureRepository lectureRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<Reservation> getReservationsInRange(LocalDateTime from, LocalDateTime to) {
        return reservationRepository.findByTimeRange(from, to);
    }

    @Transactional
    public void updateReservation(Long id, ReservationCreateDto reservationCreateDto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
        reservation.setResource(resourceRepository.findById(reservationCreateDto.getResourceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")));
        reservation.setStartTime(reservationCreateDto.getStartTime());
        reservation.setEndTime(reservationCreateDto.getEndTime());
        // Update other fields as needed (e.g., lectureId, eventId, recurrence)
        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
        reservationRepository.delete(reservation);
    }

    @Transactional
    public ReservationDto createReservation(ReservationCreateDto reservationCreateDto, Long userId) {

        log.info("User ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Resource resource = resourceRepository.findById(reservationCreateDto.getResourceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        Lecture lecture = null;
        if (reservationCreateDto.getLectureId() != null) {
            lecture = lectureRepository.findById(reservationCreateDto.getLectureId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture not found"));
        }

        Event event = null;
        if (reservationCreateDto.getEventId() != null) {
            event = eventRepository.findById(reservationCreateDto.getEventId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        }

        // Create the base reservation
        Reservation baseReservation = new Reservation();
        baseReservation.setUser(user);
        baseReservation.setResource(resource);
        baseReservation.setLecture(lecture);
        baseReservation.setEvent(event);
        baseReservation.setTitle(reservationCreateDto.getTitle());
        baseReservation.setStartTime(reservationCreateDto.getStartTime());
        baseReservation.setEndTime(reservationCreateDto.getEndTime());
        baseReservation.setStatus("CONFIRMED");

        List<Reservation> reservationsToSave = new ArrayList<>();

        // Handle recurrence
        if (reservationCreateDto.getRecurrence() != null) {
            reservationsToSave.addAll(createRecurringReservations(baseReservation, reservationCreateDto.getRecurrence()));
        } else {
            reservationsToSave.add(baseReservation);
        }

        // Check conflicts for all reservations
        for (Reservation reservation : reservationsToSave) {
            if (reservationRepository.existsByResourceAndTimeRange(
                    resource,
                    reservation.getStartTime(),
                    reservation.getEndTime())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, ("Resource is already reserved for the time slot: " +
                        reservation.getStartTime() + " to " + reservation.getEndTime()));
            }
        }

        // Save all reservations
        reservationRepository.saveAll(reservationsToSave);

        // Return DTO for the base reservation
        return mapToReservationDto(baseReservation);
    }

    private List<Reservation> createRecurringReservations(Reservation baseReservation, RecurrencePattern recurrence) {
        List<Reservation> recurringReservations = new ArrayList<>();
        LocalDateTime start = baseReservation.getStartTime();
        LocalDateTime end = baseReservation.getEndTime();
        LocalDateTime endDate = recurrence.getEndDate() != null ? recurrence.getEndDate() : start.plusYears(1); // Default to 1 year if no end date
        int interval = recurrence.getInterval();
        String frequency = recurrence.getFrequency();
        List<Integer> daysOfWeek = recurrence.getDaysOfWeek() != null ? recurrence.getDaysOfWeek() : List.of(start.getDayOfWeek().getValue() % 7);

        LocalDateTime currentStart = start;
        LocalDateTime currentEnd = end;

        while (currentStart.isBefore(endDate) || currentStart.isEqual(endDate)) {
            // For weekly, check if the current day matches any specified daysOfWeek
            if (frequency.equals("Weekly")) {
                int currentDayOfWeek = currentStart.getDayOfWeek().getValue() % 7; // 0=Sunday, 1=Monday, etc.
                if (!daysOfWeek.contains(currentDayOfWeek)) {
                    currentStart = currentStart.plusDays(1);
                    currentEnd = currentEnd.plusDays(1);
                    continue;
                }
            }

            Reservation recurringReservation = new Reservation();
            recurringReservation.setUser(baseReservation.getUser());
            recurringReservation.setResource(baseReservation.getResource());
            recurringReservation.setLecture(baseReservation.getLecture());
            recurringReservation.setEvent(baseReservation.getEvent());
            recurringReservation.setStartTime(currentStart);
            recurringReservation.setEndTime(currentEnd);
            recurringReservation.setStatus(baseReservation.getStatus());
            recurringReservations.add(recurringReservation);

            // Increment based on frequency and interval
            switch (frequency) {
                case "Daily":
                    currentStart = currentStart.plusDays(interval);
                    currentEnd = currentEnd.plusDays(interval);
                    break;
                case "Weekly":
                    currentStart = currentStart.plusWeeks(interval);
                    currentEnd = currentEnd.plusWeeks(interval);
                    break;
                case "Monthly":
                    currentStart = currentStart.plusMonths(interval);
                    currentEnd = currentEnd.plusMonths(interval);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported frequency: " + frequency);
            }
        }

        return recurringReservations;
    }

    public ReservationDto mapToReservationDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setTitle(reservation.getTitle());
        dto.setReservationId(reservation.getReservationId());
        dto.setResourceId(reservation.getResource().getResourceId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setStatus(reservation.getStatus());
        dto.setLectureId(reservation.getLecture() != null ? reservation.getLecture().getId() : null);
        dto.setEventId(reservation.getEvent() != null ? reservation.getEvent().getId() : null);
        return dto;
    }
}