package edu.kingston.smartcampus.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.kingston.smartcampus.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
