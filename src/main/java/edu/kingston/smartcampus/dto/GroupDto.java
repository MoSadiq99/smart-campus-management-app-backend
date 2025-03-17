package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupDto {
    private Long groupId;
    private String groupName;
    private Long creatorId;
    private String description;
    private LocalDateTime creationDate;
    private List<UserDto> members;
    private List<MessageDto> messages;
    private List<TaskDto> tasks;
    private List<FileDto> files;
}