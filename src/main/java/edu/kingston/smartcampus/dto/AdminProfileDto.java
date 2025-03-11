package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminProfileDto {
    @NotNull(message = "Admin title is required")
    private String adminTitle;
    private String profileImage;
}