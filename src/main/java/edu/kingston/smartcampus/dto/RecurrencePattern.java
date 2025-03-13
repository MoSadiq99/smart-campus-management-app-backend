package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class RecurrencePattern {

    private String frequency; // "Daily", "Weekly", "Monthly"
    private int recurrenceInterval; // Interval between occurrences (e.g., 1 for every day/week/month)
    private LocalDateTime endDate; // Optional end date for recurrence
    private List<Integer> daysOfWeek; // Days of the week (e.g., [1, 3] for Monday and Wednesday)
}
