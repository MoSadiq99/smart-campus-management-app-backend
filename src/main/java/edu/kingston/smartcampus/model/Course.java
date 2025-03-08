package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.model.user.Lecturer;
import edu.kingston.smartcampus.model.user.Student;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private String courseCode; // e.g., "CS101"

    @Column(nullable = false)
    private String courseName; // e.g., "Introduction to Programming"

    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer credits;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer; // The lecturer teaching this course

    @ManyToMany(mappedBy = "enrolledCourses")
    private List<Student> enrolledStudents; // Students enrolled in this course

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Subject> subjects; // Subjects/topics covered in this course

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Schedule> schedules; // Schedules related to this course
}