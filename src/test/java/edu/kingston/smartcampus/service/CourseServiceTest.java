package edu.kingston.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.kingston.smartcampus.dto.CourseCreateDto;
import edu.kingston.smartcampus.dto.CourseDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.user.Lecturer;
import edu.kingston.smartcampus.model.user.Student;
import edu.kingston.smartcampus.repository.CourseRepository;
import edu.kingston.smartcampus.repository.LecturerRepository;
import edu.kingston.smartcampus.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    private CourseCreateDto courseCreateDto;
    private Lecturer coordinator;
    private Course course;
    private List<Long> studentIds;
    private List<Student> students;

    @BeforeEach
    void setUp() {
        courseCreateDto = new CourseCreateDto();
        courseCreateDto.setCourseName("Computer Science");
        courseCreateDto.setCourseCode("CS101");
        courseCreateDto.setCordinatorId(1L);
        courseCreateDto.setDescription("Intro to CS");
        courseCreateDto.setStartDate(LocalDate.parse("2025-01-01"));
        courseCreateDto.setEndDate(LocalDate.parse("2025-06-01"));
        courseCreateDto.setCredits(3);

        Lecturer coordinator = new Lecturer();
        coordinator.setId(1L);
        coordinator.setFirstName("John");
        coordinator.setLastName("Doe");
        coordinator.setEmail("Hs4lH@example.com");

        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Computer Science");
        course.setCourseCode("CS101");
        course.setCoordinator(coordinator);
        course.setDescription("Intro to CS");
        course.setStartDate(LocalDate.parse("2025-01-01"));
        course.setEndDate(LocalDate.parse("2025-06-01"));
        course.setCredits(3);

        studentIds = Arrays.asList(3L, 4L);

        students = studentIds.stream().map(id -> {
            Student student = new Student();
            student.setId(id);
            return student;
        }).collect(Collectors.toList());
    }

    @Test
    void testCreateCourse() {
        when(lecturerRepository.getReferenceById(1L)).thenReturn(coordinator);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDto result = courseService.createCourse(courseCreateDto);

        assertNotNull(result);
        assertEquals("Computer Science", result.getCourseName());
        assertEquals(1L, result.getCordinatorId());
        assertEquals(3, result.getCredits());
        assertEquals(LocalDate.parse("2025-01-01"), result.getStartDate());
        assertEquals(LocalDate.parse("2025-06-01"), result.getEndDate());
        assertEquals("CS101", result.getCourseCode());
        assertEquals("Intro to CS", result.getDescription());
    }

    @Test
    void testEnrollStudents() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.getReferenceById(3L)).thenReturn(students.get(0));
        when(studentRepository.getReferenceById(4L)).thenReturn(students.get(1));
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        courseService.enrollStudents(1L, studentIds);

        assertEquals(2, course.getEnrolledStudents().size());
        assertEquals(3L, course.getEnrolledStudents().get(0).getId());
        assertEquals(4L, course.getEnrolledStudents().get(1).getId());

        verify(courseRepository).save(course);
    }
}
