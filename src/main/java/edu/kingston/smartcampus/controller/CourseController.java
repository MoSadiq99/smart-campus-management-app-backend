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
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getCourses() {
        List<CourseDto> courseDto = courseService.getCourses();
        return ResponseEntity.ok(courseDto);
    }

    @GetMapping("/courses/{courseCode}")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable String courseCode) {
        CourseDto courseDto = courseService.getCourseByCode(courseCode);
        return ResponseEntity.ok(courseDto);
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseCreateDto dto) {
        CourseDto courseDto = courseService.createCourse(dto);
        return ResponseEntity.ok(courseDto);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto dto) {
        CourseDto courseDto = courseService.updateCourse(id, dto);
        return ResponseEntity.ok(courseDto);
    }
}
