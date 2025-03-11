package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private Long taskId;
    private Long groupId;
    private String title;
    private String description;
    private List<Long> assignedToId;
    private String dueDate;
    private String status;
}
