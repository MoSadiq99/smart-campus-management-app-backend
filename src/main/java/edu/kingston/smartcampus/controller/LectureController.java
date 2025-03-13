package edu.kingston.smartcampus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kingston.smartcampus.dto.LectureCreateDto;
import edu.kingston.smartcampus.dto.LectureDto;
import edu.kingston.smartcampus.service.LectureService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @PostMapping("/lectures")
    public ResponseEntity<LectureDto> createLecture(@RequestBody LectureCreateDto dto) {
        LectureDto lectureDto = lectureService.createLecture(dto);
        return ResponseEntity.ok(lectureDto);
    }

    // @GetMapping("/lectures")
    // public ResponseEntity<List<LectureDto>> getLectures() {
    // List<LectureDto> lectureDtos = lectureService.getLectures();
    // return ResponseEntity.ok(lectureDtos);
    // }

    @GetMapping("/lectures")
    public ResponseEntity<List<LectureDto>> getLecturesByTime(@RequestParam @DateTimeFormat LocalDateTime from,
            @RequestParam @DateTimeFormat LocalDateTime to) {
        List<LectureDto> lectureDtos = lectureService.getLecturesByTime(from, to);
        return ResponseEntity.ok(lectureDtos);
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
