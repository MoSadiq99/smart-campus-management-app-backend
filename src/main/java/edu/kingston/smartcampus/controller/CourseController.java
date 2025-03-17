package edu.kingston.smartcampus.controller;

import edu.kingston.smartcampus.dto.CourseCreateDto;
import edu.kingston.smartcampus.dto.CourseDto;
import edu.kingston.smartcampus.dto.StudentDto;
import edu.kingston.smartcampus.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/courses/{courseId}/enrolled-students")
    public ResponseEntity<List<Long>> getEnrolledStudents(@PathVariable Long courseId) {
        List<Long> studentIds = courseService.getEnrolledStudents(courseId);
        return ResponseEntity.ok(studentIds);
    }

    @PostMapping("/courses/{courseId}/enrolled-students")
    public ResponseEntity<Void> enrollStudents(@PathVariable Long courseId, @RequestBody List<Long> studentIds) {
        courseService.enrollStudents(courseId, studentIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/enrolled-students-list")
    public ResponseEntity<List<StudentDto>> getEnrolledStudentsList(@PathVariable Long courseId) {
        List<StudentDto> enrolledStudentDtos = courseService.getEnrolledStudentsList(courseId);
        return ResponseEntity.ok(enrolledStudentDtos);
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
