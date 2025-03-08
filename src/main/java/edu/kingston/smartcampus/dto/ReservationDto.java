package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {

    private Long reservationId;
    private Long resourceId; // Required: ID of the resource being reserved
    private String title;
    private LocalDateTime startTime; // Required: Start time of the reservation
    private LocalDateTime endTime; // Required: End time of the reservation
    private Long lectureId; // Optional: ID of the associated lecture
    private Long eventId; // Optional: ID of the associated event
    private String status;
//    private RecurrencePattern recurrence; // Optional: Recurrence pattern for recurring reservations
}
