package edu.kingston.smartcampus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kingston.smartcampus.dto.StudentDto;
import edu.kingston.smartcampus.service.StudentService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getStudents() {
        List<StudentDto> studentDto = studentService.getStudents();
        return ResponseEntity.ok(studentDto);
    }

}
