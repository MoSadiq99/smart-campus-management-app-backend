package edu.kingston.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

import edu.kingston.smartcampus.model.enums.SubjectType;
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

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @ManyToMany
    @JoinTable(name = "course_subjects", joinColumns = @JoinColumn(name = "subject_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();
}