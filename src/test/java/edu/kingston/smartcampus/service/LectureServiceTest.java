package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.LectureCreateDto;
import edu.kingston.smartcampus.dto.LectureDto;
import edu.kingston.smartcampus.dto.RecurrencePattern;
import edu.kingston.smartcampus.dto.ReservationDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.Lecture;
import edu.kingston.smartcampus.model.Resource;
import edu.kingston.smartcampus.model.Subject;
import edu.kingston.smartcampus.model.user.Lecturer;
import edu.kingston.smartcampus.repository.CourseRepository;
import edu.kingston.smartcampus.repository.LectureRepository;
import edu.kingston.smartcampus.repository.LecturerRepository;
import edu.kingston.smartcampus.repository.ResourceRepository;
import edu.kingston.smartcampus.repository.SubjectRepository;
import edu.kingston.smartcampus.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LectureServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private LectureService lectureService;

    private Course course;
    private Lecturer lecturer;
    private Subject subject;
    private Resource resource;
    private Lecture lecture;
    private LectureCreateDto lectureCreateDto;
    private LectureDto lectureDto;
    private RecurrencePattern recurrencePattern;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup common mock objects
        course = new Course();
        course.setCourseId(1L);

        lecturer = new Lecturer();
        lecturer.setId(1L);

        subject = new Subject();
        subject.setSubjectId(1L);

        resource = new Resource();
        resource.setResourceId(1L);

        lecture = new Lecture();
        lecture.setId(1L);
        lecture.setTitle("Test Lecture");
        lecture.setDescription("Test Description");
        lecture.setStartTime(LocalDateTime.now());
        lecture.setEndTime(LocalDateTime.now().plusHours(1));
        lecture.setCourse(course);
        lecture.setLecturer(lecturer);
        lecture.setSubject(subject);
        lecture.setResource(resource);

        // Create RecurrencePattern object
        recurrencePattern = new RecurrencePattern();
        recurrencePattern.setFrequency("Daily");
        recurrencePattern.setRecurrenceInterval(1);
        recurrencePattern.setEndDate(LocalDateTime.now().plusDays(10));
        recurrencePattern.setDaysOfWeek(List.of(1, 3)); // Monday and Wednesday

        lectureCreateDto = new LectureCreateDto();
        lectureCreateDto.setTitle("Test Lecture");
        lectureCreateDto.setDescription("Test Description");
        lectureCreateDto.setStartTime(LocalDateTime.now());
        lectureCreateDto.setEndTime(LocalDateTime.now().plusHours(1));
        lectureCreateDto.setCourseId(1L);
        lectureCreateDto.setLecturerId(1L);
        lectureCreateDto.setSubjectId(1L);
        lectureCreateDto.setResource(1L);
        lectureCreateDto.setRecurrencePattern(recurrencePattern);

        lectureDto = new LectureDto();
        lectureDto.setTitle("Updated Test Lecture");
        lectureDto.setDescription("Updated Test Description");
        lectureDto.setStartTime(LocalDateTime.now());
        lectureDto.setEndTime(LocalDateTime.now().plusHours(1));
        lectureDto.setCourseId(1L);
        lectureDto.setLecturerId(1L);
        lectureDto.setSubjectId(1L);
        lectureDto.setResource(1L);
    }

    @Test
    void testCreateLecture() {
        when(courseRepository.getReferenceById(1L)).thenReturn(course);
        when(lecturerRepository.getReferenceById(1L)).thenReturn(lecturer);
        when(subjectRepository.getReferenceById(1L)).thenReturn(subject);
        when(resourceRepository.getReferenceById(1L)).thenReturn(resource);
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        when(reservationService.createReservation(any(), eq(1L)))
                .thenReturn(new ReservationDto());

        ReservationDto reservationDto = lectureService.createLecture(lectureCreateDto);

        assertNotNull(reservationDto);
        verify(lectureRepository, times(1)).save(any(Lecture.class));
        verify(reservationService, times(1)).createReservation(any(), eq(1L));
    }

    @Test
    void testGetLectures() {
        when(lectureRepository.findAll()).thenReturn(List.of(lecture));

        List<LectureDto> lectureDtos = lectureService.getLectures();

        assertNotNull(lectureDtos);
        assertEquals(1, lectureDtos.size());
        assertEquals("Test Lecture", lectureDtos.get(0).getTitle());
    }

    @Test
    void testUpdateLecture() {
        when(lectureRepository.getReferenceById(1L)).thenReturn(lecture);
        when(courseRepository.getReferenceById(1L)).thenReturn(course);
        when(lecturerRepository.getReferenceById(1L)).thenReturn(lecturer);
        when(subjectRepository.getReferenceById(1L)).thenReturn(subject);
        when(resourceRepository.getReferenceById(1L)).thenReturn(resource);
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        LectureDto updatedLectureDto = lectureService.updateLecture(1L, lectureDto);

        assertNotNull(updatedLectureDto);
        assertEquals("Updated Test Lecture", updatedLectureDto.getTitle());
        verify(lectureRepository, times(1)).save(any(Lecture.class));
    }
}
