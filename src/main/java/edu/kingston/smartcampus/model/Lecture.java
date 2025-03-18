package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.dto.RecurrencePattern;
import edu.kingston.smartcampus.model.user.Lecturer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Embedded
    private RecurrencePattern recurrencePattern;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = true)
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = true)
    private Resource resource;
}
