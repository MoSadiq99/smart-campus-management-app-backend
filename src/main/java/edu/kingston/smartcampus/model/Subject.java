package edu.kingston.smartcampus.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @Column(nullable = false)
    private String subjectName; // e.g., "Variables", "Loops"

    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // The course this subject belongs to
}