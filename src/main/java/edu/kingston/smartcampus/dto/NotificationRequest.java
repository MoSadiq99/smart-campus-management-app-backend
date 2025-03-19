package edu.kingston.smartcampus.dto;

import lombok.Data;

// Simple request DTO for sending notifications
@Data
public class NotificationRequest {
    private String message;
    private String type;
}