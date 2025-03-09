package edu.kingston.smartcampus.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.kingston.smartcampus.dto.CourseCreateDto;
import edu.kingston.smartcampus.dto.CourseDto;
import edu.kingston.smartcampus.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/api/courses")
    public ResponseEntity<List<CourseDto>> getCourses() {
        List<CourseDto> courseDto = courseService.getCourses();
        return ResponseEntity.ok(courseDto);
    }

    @PostMapping("/api/courses")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseCreateDto dto) {
        CourseDto courseDto = courseService.createCourse(dto);
        return ResponseEntity.ok(courseDto);
    }
}
