package edu.kingston.smartcampus.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LectureDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RecurrencePattern recurrencePattern;
    private Long courseId;
    private Long lecturerId;
    private Long subjectId;
    private Long resource;
}