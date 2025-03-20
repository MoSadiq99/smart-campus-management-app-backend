package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.*;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.enums.RoleName;
import edu.kingston.smartcampus.model.enums.UserStatus;
import edu.kingston.smartcampus.model.user.*;
import edu.kingston.smartcampus.repository.NotificationRepository;
import edu.kingston.smartcampus.repository.RoleRepository;
import edu.kingston.smartcampus.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

//
//    public NotificationDto sendNotification(NotificationCreateDto dto) {
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        Notification notification = new Notification();
//        notification.setUser(user);
//        notification.setMessage(dto.getMessage());
//        notification.setType(dto.getType());
//        notification.setSentTime(dto.getSentTime() != null ? dto.getSentTime() : LocalDateTime.now());
//        notification.setStatus("SENT");
//        notification.setRead(false);
//
//        Notification savedNotification = notificationRepository.save(notification);
//
//        // Send via WebSocket to the user's specific topic
//        NotificationDto notificationDto = mapToNotificationDto(savedNotification);
//        messagingTemplate.convertAndSend("/topic/notifications/" + dto.getUserId(), notificationDto);
//
//        return notificationDto;
//    }
//
//    public List<NotificationDto> getUserNotifications(Long userId) {
//        userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        List<Notification> notifications = notificationRepository.findByUserId(userId);
//        return notifications.stream()
//                .map(this::mapToNotificationDto)
//                .collect(Collectors.toList());
//    }
//
//    private NotificationDto mapToNotificationDto(Notification notification) {
//        NotificationDto dto = new NotificationDto();
//        dto.setNotificationId(notification.getNotificationId());
//        dto.setUserId(notification.getUser().getId());
//        dto.setMessage(notification.getMessage());
//        dto.setType(notification.getType());
//        dto.setSentTime(notification.getSentTime());
//        dto.setStatus(notification.getStatus());
//        dto.setRead(notification.isRead());
//        return dto;
//    }

    public UserDto registerUser(@Valid UserRegisterDto dto) {
        User user;
        switch (dto.getUserType().toUpperCase()) {
            case "STUDENT":
                user = new Student();
                Role studentRole = roleRepository.findByRoleName(RoleName.ROLE_STUDENT)
                        .orElseThrow(() -> new IllegalStateException("Student role not found"));
                user.setRole(studentRole);
                break;
            case "LECTURER":
                user = new Lecturer();
                Role lecturerRole = roleRepository.findByRoleName(RoleName.ROLE_LECTURER)
                        .orElseThrow(() -> new IllegalStateException("Lecturer role not found"));
                user.setRole(lecturerRole);
                break;
            case "ADMIN":
                user = new Admin();
                Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new IllegalStateException("Admin role not found"));
                user.setRole(adminRole);
                break;
            default:
                throw new IllegalArgumentException("Invalid user type: " + dto.getUserType());
        }

        // Set common fields
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setProfileImage(
                dto.getProfileImage() != null && !dto.getProfileImage().isEmpty() ? dto.getProfileImage()
                        : "default-avatar.png");
        user.setStatus(UserStatus.PENDING);
        user.setRegistrationDate(LocalDateTime.now());
        user.setAccountLocked(false);

        // Save and return DTO
        User savedUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        mapToUserDto(savedUser, userDto);
        return userDto;
    }

    @Transactional
    public LecturerDto setLecturerProfile(Long id, LecturerProfileDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if user is a Lecturer and pending
        if (!(user instanceof Lecturer lecturer)) {
            throw new IllegalStateException("User is not a Lecturer");
        }
        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("User status is already set to " + user.getStatus());
        }

        // Update existing Lecturer
        lecturer.setDepartment(dto.getDepartment());
        lecturer.setStatus(UserStatus.ACTIVE);

        User savedLecturer = userRepository.save(lecturer);
        LecturerDto lecturerDto = new LecturerDto();
        mapToLecturerDto((Lecturer) savedLecturer, lecturerDto);
        return lecturerDto;
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setProfileImage(dto.getProfileImage());

        User savedUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        mapToUserDto(savedUser, userDto);
        return userDto;
    }

    @Transactional
    public StudentDto setStudentProfile(Long id, StudentProfileDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if user is a Student and pending
        if (!(user instanceof Student student)) {
            throw new IllegalStateException("User is not a Student");
        }
        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("User status is already set to " + user.getStatus());
        }

        // Update existing Student
        student.setStudentIdNumber(dto.getStudentIdNumber());
        student.setMajor(dto.getMajor());
        student.setStatus(UserStatus.ACTIVE);

        User savedStudent = userRepository.save(student);
        StudentDto studentDto = new StudentDto();
        mapToStudentDto((Student) savedStudent, studentDto);
        return studentDto;
    }

    @Transactional
    public AdminDto setAdminProfile(Long id, AdminProfileDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if user is an Admin and pending
        if (!(user instanceof Admin admin)) {
            throw new IllegalStateException("User is not an Admin");
        }
        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("User status is already set to " + user.getStatus());
        }

        // Update existing Admin
        admin.setAdminTitle(dto.getAdminTitle());
        admin.setStatus(UserStatus.ACTIVE);

        User savedAdmin = userRepository.save(admin);
        AdminDto adminDto = new AdminDto();
        mapToAdminDto((Admin) savedAdmin, adminDto);
        return adminDto;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserDto dto = new UserDto();
        mapToUserDto(user, dto);
        return dto;
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
        // dto.setUserType(user instanceof Lecturer ? "LECTURER" : user instanceof
        // Student ? "STUDENT" : user instanceof Admin ? "ADMIN" : "PENDING");
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
        dto.setEnrolledCourseIds(
                student.getEnrolledCourses().stream().map(Course::getCourseId).collect(Collectors.toList()));
    }

    private void mapToAdminDto(Admin admin, AdminDto dto) {
        mapToUserDto(admin, dto);
        dto.setAdminTitle(admin.getAdminTitle());
    }
}