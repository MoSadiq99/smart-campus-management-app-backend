package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.user.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
