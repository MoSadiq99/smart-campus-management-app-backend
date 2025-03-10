package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseCreateDto {
    @NotNull(message = "Course code is required")
    private String courseCode;

    @NotNull(message = "Course name is required")
    private String courseName;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Integer credits;

    @NotNull(message = "Cordinator ID is required")
    private Long cordinatorId;
}
