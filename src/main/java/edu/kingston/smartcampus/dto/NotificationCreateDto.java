package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationCreateDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Message is required")
    private String message;

    private String type; // Optional: "EMAIL", "SMS", "IN_APP"

    private LocalDateTime sentTime; // Optional, defaults to now if null

    @NotNull(message = "Status is required")
    private String status;
}