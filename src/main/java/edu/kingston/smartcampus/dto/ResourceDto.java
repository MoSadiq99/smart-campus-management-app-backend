package edu.kingston.smartcampus.dto;

import lombok.Data;

@Data
public class ResourceDto {
    private Long resourceId;
    private String resourceName;
    private String type;
    private Integer capacity;
    private String status;
    private String location;
}
