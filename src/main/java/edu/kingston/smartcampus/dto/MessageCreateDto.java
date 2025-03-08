package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageCreateDto {
    @NotNull(message = "Sender ID is required")
    private Long senderId;

    @NotNull(message = "Group ID is required")
    private Long groupId;

    @NotNull(message = "Content is required")
    private String content;
}