package edu.kingston.smartcampus.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import edu.kingston.smartcampus.dto.LectureCreateDto;
import edu.kingston.smartcampus.dto.LectureDto;
import edu.kingston.smartcampus.model.Lecture;
import edu.kingston.smartcampus.model.Resource;
import edu.kingston.smartcampus.repository.CourseRepository;
import edu.kingston.smartcampus.repository.LectureRepository;
import edu.kingston.smartcampus.repository.LecturerRepository;
import edu.kingston.smartcampus.repository.ResourceRepository;
import edu.kingston.smartcampus.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;
    private final LectureRepository lectureRepository;
    private final SubjectRepository subjectRepository;
    private final ResourceRepository resourceRepository;

    public LectureDto createLecture(LectureCreateDto dto) {
        Lecture lecture = new Lecture();

        lecture.setTitle(dto.getTitle());
        lecture.setDescription(dto.getDescription());
        lecture.setStartTime(dto.getStartTime());
        lecture.setEndTime(dto.getEndTime());
        lecture.setRecurrencePattern(dto.getRecurrencePattern());
        lecture.setCourse(courseRepository.getReferenceById(dto.getCourseId()));
        lecture.setLecturer(lecturerRepository.getReferenceById(dto.getLecturerId()));
        lecture.setSubject(subjectRepository.getReferenceById(dto.getSubjectId()));
        lecture.setResources(dto.getResources().stream()
                .map(id -> resourceRepository.getReferenceById((id)))
                .collect(Collectors.toList()));

        Lecture savedLecture = lectureRepository.save(lecture);

        LectureDto lectureDto = new LectureDto();
        mapToLectureDto(savedLecture, lectureDto);
        return lectureDto;
    };

    // public List<LectureDto> getLectures() {
    // List<Lecture> lectures = lectureRepository.findAll();

    // return lectures.stream().map(lecture -> {
    // LectureDto lectureDto = new LectureDto();
    // mapToLectureDto(lecture, lectureDto);
    // return lectureDto;
    // })
    // .collect(Collectors.toList());
    // }

    public List<LectureDto> getLecturesByTime(LocalDateTime from, LocalDateTime to) {
        List<Lecture> lectures = lectureRepository.findByStartTimeBetween(from, to)
                .orElseThrow(() -> new UsernameNotFoundException("Lectures not found"));

        return lectures.stream().map(lecture -> {
            LectureDto lectureDto = new LectureDto();
            mapToLectureDto(lecture, lectureDto);
            return lectureDto;
        })
                .collect(Collectors.toList());
    }

    public LectureDto updateLecture(Long lectureId, LectureDto dto) {
        Lecture lecture = lectureRepository.getReferenceById(lectureId);

        lecture.setTitle(dto.getTitle());
        lecture.setDescription(dto.getDescription());
        lecture.setStartTime(dto.getStartTime());
        lecture.setEndTime(dto.getEndTime());
        lecture.setRecurrencePattern(dto.getRecurrencePattern());
        lecture.setCourse(courseRepository.getReferenceById(dto.getCourseId()));
        lecture.setLecturer(lecturerRepository.getReferenceById(dto.getLecturerId()));
        lecture.setSubject(subjectRepository.getReferenceById(dto.getSubjectId()));
        lecture.setResources(dto.getResources().stream()
                .map(resourceId -> resourceRepository.getReferenceById((resourceId)))
                .collect(Collectors.toList()));

        Lecture savedLecture = lectureRepository.save(lecture);

        LectureDto lectureDto = new LectureDto();
        mapToLectureDto(savedLecture, lectureDto);
        return lectureDto;
    }

    public void deleteLecture(Long lectureId) {
        lectureRepository.deleteById(lectureId);
    }

    // public List<LectureDto> getLecturesBySubject(Long subjectId) {
    // List<Lecture> lectures =
    // lectureRepository.findBySubject(subjectRepository.getReferenceById(subjectId))
    // .orElseThrow(() -> new IllegalArgumentException("Lecture not found"));

    // return lectures.stream().map(lecture -> {
    // LectureDto lectureDto = new LectureDto();
    // mapToLectureDto(lecture, lectureDto);
    // return lectureDto;
    // })
    // .collect(Collectors.toList());
    // }

    private void mapToLectureDto(Lecture lecture, LectureDto lectureDto) {
        lectureDto.setId(lecture.getId());
        lectureDto.setTitle(lecture.getTitle());
        lectureDto.setDescription(lecture.getDescription());
        lectureDto.setStartTime(lecture.getStartTime());
        lectureDto.setEndTime(lecture.getEndTime());
        lectureDto.setRecurrencePattern(lecture.getRecurrencePattern());
        lectureDto.setCourseId(lecture.getCourse().getCourseId());
        lectureDto.setLecturerId(lecture.getLecturer().getId());
        lectureDto.setSubjectId(lecture.getSubject().getSubjectId());
        lectureDto.setResources(lecture.getResources().stream()
                .map(Resource::getResourceId)
                .collect(Collectors.toList()));
    }
}
