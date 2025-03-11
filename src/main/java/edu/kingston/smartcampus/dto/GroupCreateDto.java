package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupCreateDto {
    @NotNull(message = "Group name is required")
    private String groupName;

    @NotNull(message = "Creator ID is required")
    private Long creatorId;

    private List<Long> initialMemberIds; // Optional list of user IDs

    private String description;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}