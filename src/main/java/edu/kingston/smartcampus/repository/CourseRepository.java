package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);

    Optional<Course> findByCourseId(Long courseId);
}
