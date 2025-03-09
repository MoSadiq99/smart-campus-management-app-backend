package edu.kingston.smartcampus.dto;

import java.util.List;

import lombok.Data;

@Data
public class SubjectDto {
    private Long subjectId;
    private String subjectName;
    private String description;
    private List<Long> courseId;
}
