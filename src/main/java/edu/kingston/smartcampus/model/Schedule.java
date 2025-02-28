package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; // Link to the course (nullable if not course-related)

    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String recurrence;
    private String location;
    private String status;
}