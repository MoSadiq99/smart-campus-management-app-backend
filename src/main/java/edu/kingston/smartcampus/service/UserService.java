package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.*;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.enums.RoleName;
import edu.kingston.smartcampus.model.enums.UserStatus;
import edu.kingston.smartcampus.model.user.*;
import edu.kingston.smartcampus.repository.RoleRepository;
import edu.kingston.smartcampus.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerUser(UserRegisterDto dto) {
        PendingUser user = new PendingUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setProfileImage(dto.getProfileImage());
        user.setStatus(UserStatus.PENDING);
        user.setRegistrationDate(LocalDateTime.now());
        user.setAccountLocked(false);

        Role defaultRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default role not found"));
        user.setRole(defaultRole);

        User savedUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        mapToUserDto(savedUser, userDto);
        return userDto;
    }

    public LecturerDto setLecturerProfile(Long id, LecturerProfileDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!(user instanceof PendingUser)) {
            throw new IllegalStateException("User type already set");
        }

        Lecturer lecturer = new Lecturer();
        copyCommonFields(user, lecturer);
        lecturer.setDepartment(dto.getDepartment());
        lecturer.setStatus(UserStatus.ACTIVE);
        lecturer.setRole(roleRepository.findByRoleName(RoleName.ROLE_LECTURER)
                .orElseThrow(() -> new IllegalStateException("Lecturer role not found")));

        User savedLecturer = userRepository.save(lecturer);
        userRepository.delete(user);

        LecturerDto lecturerDto = new LecturerDto();
        mapToLecturerDto((Lecturer) savedLecturer, lecturerDto);
        return lecturerDto;
    }

    public StudentDto setStudentProfile(Long id, StudentProfileDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!(user instanceof PendingUser)) {
            throw new IllegalStateException("User type already set");
        }

        Student student = new Student();
        copyCommonFields(user, student);
        student.setStudentIdNumber(dto.getStudentIdNumber());
        student.setMajor(dto.getMajor());
        student.setStatus(UserStatus.ACTIVE);
        student.setRole(roleRepository.findByRoleName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new IllegalStateException("Student role not found")));

        User savedStudent = userRepository.save(student);
        userRepository.delete(user);

        StudentDto studentDto = new StudentDto();
        mapToStudentDto((Student) savedStudent, studentDto);
        return studentDto;
    }

    public AdminDto setAdminProfile(Long id, AdminProfileDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!(user instanceof PendingUser)) {
            throw new IllegalStateException("User type already set");
        }

        Admin admin = new Admin();
        copyCommonFields(user, admin);
        admin.setAdminTitle(dto.getAdminTitle());
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRole(roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("Admin role not found")));

        User savedAdmin = userRepository.save(admin);
        userRepository.delete(user);

        AdminDto adminDto = new AdminDto();
        mapToAdminDto((Admin) savedAdmin, adminDto);
        return adminDto;
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserDto dto = new UserDto();
        mapToUserDto(user, dto);
        return dto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    private void copyCommonFields(User source, User target) {
        target.setId(source.getId());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setPassword(source.getPassword());
        target.setAddress(source.getAddress());
        target.setProfileImage(source.getProfileImage());
        target.setRegistrationDate(source.getRegistrationDate());
        target.setAccountLocked(source.isAccountLocked());
    }

    private void mapToUserDto(User user, UserDto dto) {
        dto.setUserId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setProfileImage(user.getProfileImage());
        dto.setStatus(user.getStatus().name());
        dto.setRoleName(user.getRole().getRoleName().name());
        dto.setUserType(user instanceof Lecturer ? "LECTURER" : user instanceof Student ? "STUDENT" : user instanceof Admin ? "ADMIN" : "PENDING");
    }

    private void mapToLecturerDto(Lecturer lecturer, LecturerDto dto) {
        mapToUserDto(lecturer, dto);
        dto.setDepartment(lecturer.getDepartment());
        dto.setCourseIds(lecturer.getCoursesTaught().stream().map(Course::getCourseId).collect(Collectors.toList()));
    }

    private void mapToStudentDto(Student student, StudentDto dto) {
        mapToUserDto(student, dto);
        dto.setStudentIdNumber(student.getStudentIdNumber());
        dto.setMajor(student.getMajor());
        dto.setEnrolledCourseIds(student.getEnrolledCourses().stream().map(Course::getCourseId).collect(Collectors.toList()));
    }

    private void mapToAdminDto(Admin admin, AdminDto dto) {
        mapToUserDto(admin, dto);
        dto.setAdminTitle(admin.getAdminTitle());
    }
}