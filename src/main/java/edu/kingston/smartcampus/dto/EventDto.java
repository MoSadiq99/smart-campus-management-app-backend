package edu.kingston.smartcampus.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class EventDto {
    private Long eventId;
    private Long organizerId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Integer capacity;
    private String status;
    private List<Long> attendeeIds;
}
