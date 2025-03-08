package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

}
