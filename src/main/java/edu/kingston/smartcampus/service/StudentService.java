package edu.kingston.smartcampus.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.kingston.smartcampus.dto.StudentDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.user.Student;
import edu.kingston.smartcampus.repository.StudentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<StudentDto> getStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDto> studentDtos = students.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return studentDtos;
    }

    private StudentDto mapToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setUserId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setAddress(student.getAddress());
        dto.setProfileImage(student.getProfileImage());
        dto.setStudentIdNumber(student.getStudentIdNumber());
        dto.setMajor(student.getMajor());
        dto.setEnrolledCourseIds(student.getEnrolledCourses().stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList()));
        return dto;
    }
}
