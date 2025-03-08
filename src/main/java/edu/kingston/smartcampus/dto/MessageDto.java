package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {
    private Long messageId;
    private Long senderId;
    private Long groupId;
    private String content;
    private LocalDateTime sentTime;
}