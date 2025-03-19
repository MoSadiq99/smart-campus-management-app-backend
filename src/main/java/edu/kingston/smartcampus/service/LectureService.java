package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.LectureCreateDto;
import edu.kingston.smartcampus.dto.LectureDto;
import edu.kingston.smartcampus.dto.ReservationCreateDto;
import edu.kingston.smartcampus.dto.ReservationDto;
import edu.kingston.smartcampus.model.Lecture;
import edu.kingston.smartcampus.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {
    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;
    private final LectureRepository lectureRepository;
    private final SubjectRepository subjectRepository;
    private final ResourceRepository resourceRepository;
    private final ReservationService reservationService;

    public ReservationDto createLecture(LectureCreateDto dto) {
        Lecture lecture = new Lecture();

        lecture.setTitle(dto.getTitle());
        lecture.setDescription(dto.getDescription());
        lecture.setStartTime(dto.getStartTime());
        lecture.setEndTime(dto.getEndTime());
        lecture.setRecurrencePattern(dto.getRecurrencePattern() != null ? dto.getRecurrencePattern() : null);
        lecture.setCourse(courseRepository.getReferenceById(dto.getCourseId()));
        lecture.setLecturer(lecturerRepository.getReferenceById(dto.getLecturerId()));
        lecture.setSubject(subjectRepository.getReferenceById(dto.getSubjectId()));
        lecture.setResource(resourceRepository.getReferenceById(dto.getResource()));

        Lecture savedLecture = lectureRepository.save(lecture);

        ReservationCreateDto reservationCreateDto = new ReservationCreateDto();
        reservationCreateDto.setLectureId(savedLecture.getId());
        reservationCreateDto.setStartTime(dto.getStartTime());
        reservationCreateDto.setEndTime(dto.getEndTime());
        reservationCreateDto.setTitle(dto.getTitle());
        reservationCreateDto.setResourceId(dto.getResource());
        reservationCreateDto.setRecurrence(dto.getRecurrencePattern() != null ? dto.getRecurrencePattern() : null);

        try {
            ReservationDto reservationDto = reservationService.createReservation(reservationCreateDto,
                    dto.getLecturerId());
            return reservationDto;
        } catch (Exception e) {
            lectureRepository.delete(savedLecture);
            throw new RuntimeException(e);
        }
    };

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

    public List<LectureDto> getLectures() {
        List<Lecture> lectures = lectureRepository.findAll();
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
        lecture.setResource(resourceRepository.getReferenceById(dto.getResource()));

        Lecture savedLecture = lectureRepository.save(lecture);

        LectureDto lectureDto = new LectureDto();
        mapToLectureDto(savedLecture, lectureDto);
        return lectureDto;
    }

    public void deleteLecture(Long lectureId) {
        lectureRepository.deleteById(lectureId);
    }

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
        lectureDto.setResource(lecture.getResource().getResourceId());
    }
}
