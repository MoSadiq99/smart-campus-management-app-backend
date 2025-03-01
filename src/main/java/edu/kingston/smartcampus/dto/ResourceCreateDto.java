package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceCreateDto {
    @NotNull(message = "Resource name is required")
    private String resourceName;

    @NotNull(message = "Type is required")
    private String type;

    private Integer capacity;

    @NotNull(message = "Status is required")
    private String status;

    private String location;
}
