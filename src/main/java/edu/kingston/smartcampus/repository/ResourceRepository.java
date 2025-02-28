package edu.kingston.smartcampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kingston.smartcampus.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

}