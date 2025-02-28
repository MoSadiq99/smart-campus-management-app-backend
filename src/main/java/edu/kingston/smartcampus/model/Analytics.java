package edu.kingston.smartcampus.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "analytics")
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analyticsId;

    private String type; // e.g., "ResourceUsage"
    private String data; // JSON or metric values
    private LocalDateTime recordedTime;
}