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
    private List<Long> memberIds;
    private List<MessageDto> messages;
}