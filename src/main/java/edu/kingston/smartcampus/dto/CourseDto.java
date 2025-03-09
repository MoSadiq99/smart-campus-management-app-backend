package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseDto {
    private Long courseId;
    private String courseCode;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer credits;
    private Long cordinatorId;
    private List<Long> enrolledStudentIds;
    private List<Long> subjectIds;
}
