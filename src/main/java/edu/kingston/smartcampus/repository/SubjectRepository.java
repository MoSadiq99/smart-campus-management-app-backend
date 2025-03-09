package edu.kingston.smartcampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kingston.smartcampus.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
