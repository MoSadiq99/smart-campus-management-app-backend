package edu.kingston.smartcampus.service;

import org.springframework.stereotype.Service;

import edu.kingston.smartcampus.dto.ResourceCreateDto;
import edu.kingston.smartcampus.dto.ResourceDto;
import edu.kingston.smartcampus.model.Resource;
import edu.kingston.smartcampus.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceDto createResource(ResourceCreateDto dto) {
        Resource resource = new Resource();
        resource.setResourceName(dto.getResourceName());
        resource.setType(dto.getType());
        resource.setCapacity(dto.getCapacity());
        resource.setAvailabilityStatus(dto.getStatus());
        resource.setLocation(dto.getLocation());

        Resource savedResource = resourceRepository.save(resource);

        ResourceDto resourceDto = new ResourceDto();
        mapToResourceDto(savedResource, resourceDto);
        return resourceDto;
    }

    public ResourceDto getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        ResourceDto resourceDto = new ResourceDto();
        mapToResourceDto(resource, resourceDto);
        return resourceDto;
    }

    private void mapToResourceDto(Resource resource, ResourceDto resourceDto) {
        resourceDto.setResourceId(resource.getResourceId());
        resourceDto.setResourceName(resource.getResourceName());
        resourceDto.setType(resource.getType());
        resourceDto.setCapacity(resource.getCapacity());
        resourceDto.setStatus(resource.getAvailabilityStatus());
        resourceDto.setLocation(resource.getLocation());
    }
}
