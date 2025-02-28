package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LecturerProfileDto {
    @NotNull(message = "Department is required")
    private String department;

    @NotNull(message = "Office location is required")
    private String officeLocation;
}