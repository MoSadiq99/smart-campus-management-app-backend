package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.LecturerDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.user.Lecturer;
import edu.kingston.smartcampus.repository.LecturerRepository;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;

    public LecturerDto getLecturerById(Long id) {
        Lecturer lecturer = lecturerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        LecturerDto dto = new LecturerDto();
        mapToLecturerDto(lecturer, dto);
        return dto;
    }

    private void mapToLecturerDto(Lecturer lecturer, LecturerDto dto) {
        dto.setUserId(lecturer.getId());
        dto.setFirstName(lecturer.getFirstName());
        dto.setLastName(lecturer.getLastName());
        dto.setEmail(lecturer.getEmail());
        dto.setPhone(lecturer.getPhone());
        dto.setAddress(lecturer.getAddress());
        dto.setRoleName(lecturer.getRole().getRoleName().name());
        dto.setStatus(lecturer.getStatus().name());
        dto.setProfileImage(lecturer.getProfileImage());
        dto.setDepartment(lecturer.getDepartment());
        dto.setCourseIds(lecturer.getCoursesTaught().stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList()));
    }
}