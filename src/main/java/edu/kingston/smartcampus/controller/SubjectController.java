package edu.kingston.smartcampus.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.kingston.smartcampus.dto.SubjectCreateDto;
import edu.kingston.smartcampus.dto.SubjectDto;
import edu.kingston.smartcampus.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectDto>> getSubjects() {
        List<SubjectDto> subjectDtos = subjectService.getSubjects();
        return ResponseEntity.ok(subjectDtos);
    }

    @PostMapping("/subjects")
    public ResponseEntity<SubjectDto> createCourse(@Valid @RequestBody SubjectCreateDto dto) {
        SubjectDto subjectDto = subjectService.createSubject(dto);
        return ResponseEntity.ok(subjectDto);
    }
}
