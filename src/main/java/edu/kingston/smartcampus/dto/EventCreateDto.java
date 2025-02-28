package edu.kingston.smartcampus.dto;

import java.time.LocalDateTime;
import edu.kingston.smartcampus.model.enums.EventStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventCreateDto {
    @NotNull(message = "Organizer ID is required")
    private Long organizerId;

    @NotNull(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Location is required")
    private String location;

    private Integer capacity;

    @NotNull(message = "Status is required")
    private EventStatus status;
}
