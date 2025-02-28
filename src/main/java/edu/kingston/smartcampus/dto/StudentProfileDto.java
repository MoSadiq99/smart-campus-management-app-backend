package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentProfileDto {
    @NotNull(message = "Student ID number is required")
    private String studentIdNumber;

    @NotNull(message = "Major is required")
    private String major;
}