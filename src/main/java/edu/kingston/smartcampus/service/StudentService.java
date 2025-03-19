package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.StudentDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.enums.UserStatus;
import edu.kingston.smartcampus.model.user.Student;
import edu.kingston.smartcampus.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow();
        return mapToDto(student);
    }

    public StudentDto updateStudent(Long id, StudentDto dto) {
        Student student = studentRepository.findById(id).orElseThrow();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setAddress(dto.getAddress());
        student.setProfileImage(dto.getProfileImage());
        student.setStudentIdNumber(dto.getStudentIdNumber());
        student.setMajor(dto.getMajor());
        student.setStatus(UserStatus.valueOf(dto.getStatus()));
        studentRepository.save(student);
        return mapToDto(student);
    }

    private StudentDto mapToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setUserId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setAddress(student.getAddress());
        dto.setRoleName(student.getRole().getRoleName().name());
        dto.setStatus(student.getStatus().name());
        dto.setProfileImage(student.getProfileImage());
        dto.setStudentIdNumber(student.getStudentIdNumber());
        dto.setMajor(student.getMajor());
        dto.setEnrolledCourseIds(student.getEnrolledCourses().stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList()));
        return dto;
    }

    public List<StudentDto> getEnrolledStudentsByCourse(Long courseId) {
        List<Student> students = studentRepository.findAll();
        List<StudentDto> enrollStudentDtos = students.stream()
                .filter(student -> student.getEnrolledCourses().stream()
                        .anyMatch(course -> course.getCourseId().equals(courseId)))
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return enrollStudentDtos;
    }
}
