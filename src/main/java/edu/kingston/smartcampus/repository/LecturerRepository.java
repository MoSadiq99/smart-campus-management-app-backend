package edu.kingston.smartcampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kingston.smartcampus.model.user.Lecturer;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
}
