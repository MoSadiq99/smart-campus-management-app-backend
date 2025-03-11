package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDto {
    private Long fileId;
    private Long groupId;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadTime;
    private Long uploaderId; // Add this field
}
