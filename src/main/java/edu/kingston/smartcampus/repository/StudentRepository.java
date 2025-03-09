package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.user.Student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
