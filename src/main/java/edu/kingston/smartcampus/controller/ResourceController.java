package edu.kingston.smartcampus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.kingston.smartcampus.dto.ResourceCreateDto;
import edu.kingston.smartcampus.dto.ResourceDto;
import edu.kingston.smartcampus.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/api/resources")
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceCreateDto dto) {
        ResourceDto resourceDto = resourceService.createResource(dto);
        return ResponseEntity.ok(resourceDto);
    }

    @GetMapping("/api/resources/{id}")
    public ResponseEntity<ResourceDto> getResourceById(@PathVariable Long id) {
        ResourceDto resourceDto = resourceService.getResourceById(id);
        return ResponseEntity.ok(resourceDto);
    }

}
