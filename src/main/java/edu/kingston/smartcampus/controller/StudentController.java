package edu.kingston.smartcampus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kingston.smartcampus.dto.StudentDto;
import edu.kingston.smartcampus.service.StudentService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        StudentDto studentDto = studentService.getStudentById(id);
        return ResponseEntity.ok(studentDto);
    }

    @PutMapping("students/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto dto) {
        StudentDto studentDto = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(studentDto);
    }
}
