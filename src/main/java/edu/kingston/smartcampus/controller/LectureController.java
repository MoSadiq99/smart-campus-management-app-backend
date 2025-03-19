package edu.kingston.smartcampus.controller;

import edu.kingston.smartcampus.dto.LectureCreateDto;
import edu.kingston.smartcampus.dto.LectureDto;
import edu.kingston.smartcampus.dto.ReservationCreateDto;
import edu.kingston.smartcampus.dto.ReservationDto;
import edu.kingston.smartcampus.model.Reservation;
import edu.kingston.smartcampus.service.LectureService;
import edu.kingston.smartcampus.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final ReservationService reservationService;

    @PostMapping("/lectures")
    public ResponseEntity<ReservationDto> createLecture(@RequestBody LectureCreateDto dto) {
        ReservationDto reservationDto = lectureService.createLecture(dto);
        return ResponseEntity.ok(reservationDto);
    }

    @GetMapping("/lectures")
    public ResponseEntity<List<LectureDto>> getLecturesByTime() {
        List<LectureDto> lectureDtos = lectureService.getLectures();
        return ResponseEntity.ok(lectureDtos);
    }

    @GetMapping("/lectures/reservations")
    public ResponseEntity<List<ReservationDto>> getReservations(
            @RequestParam("from") String from,
            @RequestParam("to") String to) {
        LocalDateTime start = LocalDateTime.parse(from);
        LocalDateTime end = LocalDateTime.parse(to);
        List<Reservation> reservations = reservationService.getReservationsInRange(start, end);
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservationService::mapToReservationDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDtos);
    }

    @PostMapping("/lectures/reservations")
    public ResponseEntity<ReservationDto> createReservation(
            @RequestBody ReservationCreateDto reservationCreateDto,
            @RequestParam("userId") Long userId) {
        ReservationDto reservationDto = reservationService.createReservation(reservationCreateDto, userId);
        return ResponseEntity.ok(reservationDto);
    }

    @PutMapping("lectures/{lectureId}")
    public ResponseEntity<LectureDto> updateLecture(@PathVariable Long lectureId, @RequestBody LectureDto dto) {
        LectureDto lectureDto = lectureService.updateLecture(lectureId, dto);
        return ResponseEntity.ok(lectureDto);
    }

    @DeleteMapping("/lectures/{lectureId}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.ok().build();
    }
}
