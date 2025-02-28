package edu.kingston.smartcampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kingston.smartcampus.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
