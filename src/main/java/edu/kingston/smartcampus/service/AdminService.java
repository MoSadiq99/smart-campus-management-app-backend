package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.AdminDto;
import edu.kingston.smartcampus.model.user.Admin;
import edu.kingston.smartcampus.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminDto getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        AdminDto dto = new AdminDto();
        mapToAdminDto(admin, dto);
        return dto;
    }

    private void mapToAdminDto(Admin admin, AdminDto dto) {
        dto.setUserId(admin.getId());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setEmail(admin.getEmail());
        dto.setPhone(admin.getPhone());
        dto.setAddress(admin.getAddress());
        dto.setRoleName(admin.getRole().getRoleName().name());
        dto.setStatus(admin.getStatus().name());
        dto.setProfileImage(admin.getProfileImage());
        dto.setAdminTitle(admin.getAdminTitle());
    }
}