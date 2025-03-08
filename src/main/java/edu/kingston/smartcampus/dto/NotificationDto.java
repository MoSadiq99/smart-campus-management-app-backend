package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long notificationId;
    private Long userId;
    private String message;
    private String type;
    private LocalDateTime sentTime;
    private String status;
    private boolean read;
}