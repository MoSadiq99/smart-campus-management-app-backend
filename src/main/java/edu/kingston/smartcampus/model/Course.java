package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.model.user.Student;
import edu.kingston.smartcampus.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false, unique = true)
    private String courseCode; // e.g., "CS101"

    @Column(nullable = false)
    private String courseName; // e.g., "Introduction to Programming"

    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer credits;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private User coordinator; // Changed to User instead of Lecturer - SDQ

    @ManyToMany(mappedBy = "enrolledCourses")
    private List<Student> enrolledStudents = new ArrayList<>(); // Students enrolled in this course

    @ManyToMany
    @JoinTable(
            name = "course_subjects",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>(); // Schedules related to this course
}