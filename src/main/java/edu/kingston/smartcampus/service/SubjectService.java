package edu.kingston.smartcampus.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import edu.kingston.smartcampus.dto.SubjectCreateDto;
import edu.kingston.smartcampus.dto.SubjectDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.Subject;
import edu.kingston.smartcampus.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectDto createSubject(SubjectCreateDto dto) {
        Subject subject = new Subject();
        subject.setSubjectName(dto.getSubjectName());
        subject.setDescription(dto.getDescription());

        Subject savedSubject = subjectRepository.save(subject);

        SubjectDto subjectDto = new SubjectDto();
        mapToSubjectDto(savedSubject, subjectDto);
        return subjectDto;
    };

    public List<SubjectDto> getSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream().map(subject -> {
            SubjectDto subjectDto = new SubjectDto();
            mapToSubjectDto(subject, subjectDto);
            return subjectDto;
        }).collect(Collectors.toList());

    }

    private void mapToSubjectDto(Subject subject, SubjectDto subjectDto) {
        subjectDto.setSubjectId(subject.getSubjectId());
        subjectDto.setSubjectName(subject.getSubjectName());
        subjectDto.setDescription(subject.getDescription());
        subjectDto.setCourseId(subject.getCourses().stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList()));
    }
}
