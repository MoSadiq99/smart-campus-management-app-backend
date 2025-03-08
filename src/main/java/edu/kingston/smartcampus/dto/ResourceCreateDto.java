package edu.kingston.smartcampus.dto;

import edu.kingston.smartcampus.model.enums.ResourceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;

@Data
public class ResourceCreateDto {
    @NotNull(message = "Resource name is required")
    private String resourceName;

    @NotNull(message = "Type is required")
    private String type;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private ResourceStatus availabilityStatus;

    private String location;

}
