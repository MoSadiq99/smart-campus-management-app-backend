package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.Lecture;
import edu.kingston.smartcampus.model.Subject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<List<Lecture>> findBySubject(Subject subject);

    Optional<List<Lecture>> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);

    Optional<List<Lecture>> findByStartTimeBetweenAndResource(LocalDateTime from, LocalDateTime to, Subject subject);
}
