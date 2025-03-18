package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.model.enums.SubjectType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_subjects", joinColumns = @JoinColumn(name = "subject_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();
}