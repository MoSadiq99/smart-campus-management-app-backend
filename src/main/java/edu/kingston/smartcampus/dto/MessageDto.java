package edu.kingston.smartcampus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long messageId;
    private Long senderId;
    private Long groupId;
    private String content;
    private LocalDateTime sentTime;
}