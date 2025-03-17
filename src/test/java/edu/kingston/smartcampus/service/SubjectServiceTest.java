package edu.kingston.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.kingston.smartcampus.dto.SubjectCreateDto;
import edu.kingston.smartcampus.dto.SubjectDto;
import edu.kingston.smartcampus.model.Subject;
import edu.kingston.smartcampus.repository.SubjectRepository;

public class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private SubjectCreateDto subjectCreateDto;
    private SubjectDto subjectDto;
    private Subject existingSubject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        subjectCreateDto = new SubjectCreateDto();
        subjectCreateDto.setSubjectName("Math");
        subjectCreateDto.setDescription("Mathematics");

        subjectDto = new SubjectDto();
        subjectDto.setSubjectName("Math");
        subjectDto.setDescription("Mathematics");

        existingSubject = new Subject();
        existingSubject.setSubjectId(1L);
        existingSubject.setSubjectName("Math");
        existingSubject.setDescription("Mathematics");
        existingSubject.setCourses(new ArrayList<>());
    }

    @Test
    void testCreateSubject() {
        when(subjectRepository.save(any(Subject.class))).thenReturn(existingSubject);

        SubjectDto result = subjectService.createSubject(subjectCreateDto);

        assertNotNull(result);
        assertEquals(subjectCreateDto.getSubjectName(), result.getSubjectName());
        assertEquals(subjectCreateDto.getDescription(), result.getDescription());
    }

    @Test
    void testGetSubjects() {
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(existingSubject);
        when(subjectRepository.findAll()).thenReturn(subjectList);

        List<SubjectDto> result = subjectService.getSubjects();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(existingSubject.getSubjectName(), result.get(0).getSubjectName());
        assertEquals(existingSubject.getDescription(), result.get(0).getDescription());
    }

    @Test
    void testUpdateSubject() {
        SubjectDto updatedSubjectDto = new SubjectDto();
        updatedSubjectDto.setSubjectName("Advanced Math");
        updatedSubjectDto.setDescription("Advanced Mathematics");

        when(subjectRepository.findById(existingSubject.getSubjectId())).thenReturn(Optional.of(existingSubject));
        when(subjectRepository.save(any(Subject.class))).thenReturn(existingSubject);

        SubjectDto result = subjectService.updateSubject(existingSubject.getSubjectId(), updatedSubjectDto);

        assertNotNull(result);
        assertEquals(updatedSubjectDto.getSubjectName(), result.getSubjectName());
        assertEquals(updatedSubjectDto.getDescription(), result.getDescription());
    }
}
