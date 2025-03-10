package edu.kingston.smartcampus.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import edu.kingston.smartcampus.dto.CourseCreateDto;
import edu.kingston.smartcampus.dto.CourseDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.Subject;
import edu.kingston.smartcampus.model.user.Student;
import edu.kingston.smartcampus.repository.CourseRepository;
import edu.kingston.smartcampus.repository.LecturerRepository;
import edu.kingston.smartcampus.repository.StudentRepository;
import edu.kingston.smartcampus.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
        private final LecturerRepository lecturerRepository;
        private final CourseRepository courseRepository;
        private final StudentRepository studentRepository;
        private final SubjectRepository subjectRepository;

        public CourseDto createCourse(CourseCreateDto dto) {
                Course course = new Course();
                course.setCourseName(dto.getCourseName());
                course.setCourseCode(dto.getCourseCode());
                course.setCoordinator(lecturerRepository.getReferenceById(dto.getCordinatorId()));
                course.setDescription(dto.getDescription());
                course.setStartDate(dto.getStartDate());
                course.setEndDate(dto.getEndDate());
                course.setCredits(dto.getCredits());

                Course savedCourse = courseRepository.save(course);

                CourseDto courseDto = new CourseDto();
                mapToCourseDto(savedCourse, courseDto);
                return courseDto;
        };

        public List<CourseDto> getCourses() {
                List<Course> courses = courseRepository.findAll();
                return courses.stream()
                                .map(course -> {
                                        CourseDto courseDto = new CourseDto();
                                        mapToCourseDto(course, courseDto);
                                        return courseDto;
                                })
                                .collect(Collectors.toList());
        }

        public CourseDto getCourseByCode(String courseCode) {
                Course course = courseRepository.findByCourseCode(courseCode)
                                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
                CourseDto courseDto = new CourseDto();
                mapToCourseDto(course, courseDto);
                return courseDto;
        }

        public CourseDto updateCourse(Long courseId, CourseDto dto) {
                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
                course.setCourseName(dto.getCourseName());
                course.setCourseCode(dto.getCourseCode());
                course.setCoordinator(lecturerRepository.getReferenceById(dto.getCordinatorId()));
                course.setDescription(dto.getDescription());
                course.setStartDate(dto.getStartDate());
                course.setEndDate(dto.getEndDate());
                course.setCredits(dto.getCredits());
                course.setEnrolledStudents(dto.getEnrolledStudentIds().stream()
                                .map(id -> studentRepository.getReferenceById(id))
                                .collect(Collectors.toList()));
                course.setSubjects(dto.getSubjectIds().stream()
                                .map(id -> subjectRepository.getReferenceById(id))
                                .collect(Collectors.toList()));

                Course savedCourse = courseRepository.save(course);
                CourseDto courseDto = new CourseDto();
                mapToCourseDto(savedCourse, courseDto);
                return courseDto;
        }

        private void mapToCourseDto(Course course, CourseDto courseDto) {
                courseDto.setCourseId(course.getCourseId());
                courseDto.setCourseName(course.getCourseName());
                courseDto.setCourseCode(course.getCourseCode());
                courseDto.setDescription(course.getDescription());
                courseDto.setStartDate(course.getStartDate());
                courseDto.setEndDate(course.getEndDate());
                courseDto.setCordinatorId(course.getCoordinator().getId());
                courseDto.setCredits(course.getCredits());
                courseDto.setEnrolledStudentIds(course.getEnrolledStudents().stream()
                                .map(Student::getId)
                                .collect(Collectors.toList()));
                courseDto.setSubjectIds(course.getSubjects().stream()
                                .map(Subject::getSubjectId)
                                .collect(Collectors.toList()));
        }
}
