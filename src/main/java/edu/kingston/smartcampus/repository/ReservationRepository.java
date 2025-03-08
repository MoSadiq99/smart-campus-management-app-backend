package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.Reservation;
import edu.kingston.smartcampus.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.resource = :resource " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime")
    boolean existsByResourceAndTimeRange(
            @Param("resource") Resource resource,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    //! Same as above
    boolean existsByResourceAndStartTimeLessThanAndEndTimeGreaterThan(
            Resource resource,
            LocalDateTime endTime,
            LocalDateTime startTime);


    @Query("SELECT r FROM Reservation r WHERE r.startTime >= :from AND r.endTime <= :to")
    List<Reservation> findByTimeRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
