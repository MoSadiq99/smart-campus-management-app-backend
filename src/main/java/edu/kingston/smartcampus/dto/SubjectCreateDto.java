package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectCreateDto {
    @NotNull(message = "Subject name is required")
    private String subjectName;
    private String description;
}
