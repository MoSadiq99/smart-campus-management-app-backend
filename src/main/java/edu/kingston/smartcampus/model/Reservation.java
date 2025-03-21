package edu.kingston.smartcampus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.kingston.smartcampus.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Back reference to User
    private User user;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = true) // Optional
    private Lecture lecture; // Associated lecture (if applicable)

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = true) // Optional
    private Event event; // Associated event (if applicable)

    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // e.g., PENDING, CONFIRMED, CANCELLED
}