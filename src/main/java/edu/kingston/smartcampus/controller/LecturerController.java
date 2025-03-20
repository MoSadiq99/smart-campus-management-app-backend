package edu.kingston.smartcampus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kingston.smartcampus.dto.LecturerDto;
import edu.kingston.smartcampus.service.LecturerService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lecturers")
public class LecturerController {
    private final LecturerService lecturerService;

    @GetMapping("/{id}")
    public ResponseEntity<LecturerDto> getAdminById(@PathVariable Long id) {
        LecturerDto lectureDto = lecturerService.getLecturerById(id);
        return ResponseEntity.ok(lectureDto);
    }
}
