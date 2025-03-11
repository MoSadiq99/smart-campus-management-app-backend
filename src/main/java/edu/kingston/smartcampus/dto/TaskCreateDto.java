package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateDto {
    private String title;
    private String description;
    private Long assignedToGroupId;
    private Long assignedToId;
    private LocalDateTime dueDate;
}