package edu.kingston.smartcampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kingston.smartcampus.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
