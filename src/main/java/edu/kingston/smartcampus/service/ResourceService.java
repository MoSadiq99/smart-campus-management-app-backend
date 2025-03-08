package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.ResourceCreateDto;
import edu.kingston.smartcampus.dto.ResourceDto;
import edu.kingston.smartcampus.model.Resource;
import edu.kingston.smartcampus.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceDto createResource(ResourceCreateDto dto) {
        log.info("Creating resource: {}", dto);
        Resource resource = new Resource();
        resource.setResourceName(dto.getResourceName());
        resource.setType(dto.getType());
        resource.setCapacity(dto.getCapacity());
        resource.setAvailabilityStatus(dto.getAvailabilityStatus());
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

    public List<ResourceDto> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::mapToResourceDto)
                .collect(Collectors.toList());
    }


    private ResourceDto mapToResourceDto(Resource resource) {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setResourceId(resource.getResourceId());
        resourceDto.setResourceName(resource.getResourceName());
        resourceDto.setType(resource.getType());
        resourceDto.setCapacity(resource.getCapacity());
        resourceDto.setAvailabilityStatus(String.valueOf(resource.getAvailabilityStatus()));
        resourceDto.setLocation(resource.getLocation());
        return resourceDto;
    }

    private void mapToResourceDto(Resource resource, ResourceDto resourceDto) {
        resourceDto.setResourceId(resource.getResourceId());
        resourceDto.setResourceName(resource.getResourceName());
        resourceDto.setType(resource.getType());
        resourceDto.setCapacity(resource.getCapacity());
        resourceDto.setAvailabilityStatus(String.valueOf(resource.getAvailabilityStatus()));
        resourceDto.setLocation(resource.getLocation());
    }

    public ResourceDto updateResource(Long id, ResourceCreateDto dto) {
        Optional<Resource> resource = resourceRepository.findById(id);

        if (resource.isPresent()) {
            Resource existingResource = resource.get();
            existingResource.setResourceName(dto.getResourceName());
            existingResource.setType(dto.getType());
            existingResource.setCapacity(dto.getCapacity());
            existingResource.setAvailabilityStatus(dto.getAvailabilityStatus());
            existingResource.setLocation(dto.getLocation());

            Resource updatedResource = resourceRepository.save(existingResource);

            ResourceDto resourceDto = new ResourceDto();
            mapToResourceDto(updatedResource, resourceDto);
            return resourceDto;
        } else {
            throw new IllegalArgumentException("Resource not found");
        }

    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
}
