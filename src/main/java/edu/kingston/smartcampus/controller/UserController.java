package edu.kingston.smartcampus.controller;

import edu.kingston.smartcampus.dto.*;
import edu.kingston.smartcampus.security.JwtService;
import edu.kingston.smartcampus.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/api/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody UserRegisterDto dto) {
        UserDto userDto = userService.registerUser(dto);
        String token = jwtService.generateToken(userService.loadUserByUsername(userDto.getEmail()));
        return ResponseEntity.ok(new AuthResponseDto(token, userDto));
    }

    @PostMapping("/api/authenticate")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        UserDto userDto = userService.getUserByEmail(dto.getEmail());
        return ResponseEntity.ok(new AuthResponseDto(token, userDto));
    }

    @PutMapping("/api/users/{id}/lecturer-profile")
    public ResponseEntity<LecturerDto> setLecturerProfile(
            @PathVariable Long id, @Valid @RequestBody LecturerProfileDto dto) {
        LecturerDto lecturerDto = userService.setLecturerProfile(id, dto);
        return ResponseEntity.ok(lecturerDto);
    }

    @PutMapping("/api/users/{id}/student-profile")
    public ResponseEntity<StudentDto> setStudentProfile(
            @PathVariable Long id, @Valid @RequestBody StudentProfileDto dto) {
        StudentDto studentDto = userService.setStudentProfile(id, dto);
        return ResponseEntity.ok(studentDto);
    }

    @PutMapping("/api/users/{id}/admin-profile")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdminDto> setAdminProfile(
            @PathVariable Long id, @Valid @RequestBody AdminProfileDto dto) {
        AdminDto adminDto = userService.setAdminProfile(id, dto);
        return ResponseEntity.ok(adminDto);
    }
}