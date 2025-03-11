package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//    // Find user type Student
//    @NativeQuery("SELECT * FROM user u WHERE u.user_type = 'STUDENT'")
//    List<User> findAllStudents();
}