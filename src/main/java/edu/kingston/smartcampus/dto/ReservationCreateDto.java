package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationCreateDto {

    private String title;
    private Long resourceId; // Required: ID of the resource being reserved
    private LocalDateTime startTime; // Required: Start time of the reservation
    private LocalDateTime endTime; // Required: End time of the reservation
    private Long lectureId; // Optional: ID of the associated lecture
    private Long eventId; // Optional: ID of the associated event
    private RecurrencePattern recurrence; // Optional: Recurrence pattern for recurring reservations
}
