package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupCreateDto {
    @NotNull(message = "Group name is required")
    private String groupName;

    @NotNull(message = "Creator ID is required")
    private Long creatorId;

    private String description;
}