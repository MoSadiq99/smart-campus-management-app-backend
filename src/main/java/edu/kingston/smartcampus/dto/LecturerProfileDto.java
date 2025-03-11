package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LecturerProfileDto {
    @NotNull(message = "Department is required")
    private String department;
    private String profileImage;
}