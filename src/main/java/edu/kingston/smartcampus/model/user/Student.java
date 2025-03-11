package edu.kingston.smartcampus.model.user;

import edu.kingston.smartcampus.model.Course;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    private String studentIdNumber; // Unique student ID (e.g., "K1234567")
    private String major; // e.g., "Computer Science"

    @ManyToMany
    @JoinTable(name = "student_courses", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> enrolledCourses; // Courses this student is enrolled in
}