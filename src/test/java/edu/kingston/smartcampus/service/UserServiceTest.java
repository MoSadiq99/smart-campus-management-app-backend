package edu.kingston.smartcampus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.kingston.smartcampus.dto.NotificationCreateDto;
import edu.kingston.smartcampus.dto.NotificationDto;
import edu.kingston.smartcampus.dto.UserDto;
import edu.kingston.smartcampus.dto.UserRegisterDto;
import edu.kingston.smartcampus.model.Notification;
import edu.kingston.smartcampus.model.enums.RoleName;
import edu.kingston.smartcampus.model.user.Admin;
import edu.kingston.smartcampus.model.user.Role;
import edu.kingston.smartcampus.repository.NotificationRepository;
import edu.kingston.smartcampus.repository.RoleRepository;
import edu.kingston.smartcampus.repository.UserRepository;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private UserService userService;

    private UserRegisterDto adminDto;
    private Role adminRole;

    private Admin adminUser;
    private NotificationCreateDto notificationDto;
    private Notification notification1, notification2;

    @BeforeEach
    void setUp() {
        adminDto = new UserRegisterDto();
        adminDto.setUserType("ADMIN");
        adminDto.setFirstName("John");
        adminDto.setLastName("Doe");
        adminDto.setEmail("admin@example.com");
        adminDto.setPhone("1234567890");
        adminDto.setPassword("securepassword");
        adminDto.setAddress("123 Admin Street");

        adminRole = new Role();
        adminRole.setRoleName(RoleName.ROLE_ADMIN);

        adminUser = new Admin();
        adminUser.setId(1L);
        adminUser.setFirstName("John");
        adminUser.setLastName("Doe");
        adminUser.setEmail("admin@example.com");

        // Mock Notification DTO
        notificationDto = new NotificationCreateDto();
        notificationDto.setUserId(1L);
        notificationDto.setMessage("System update scheduled");
        notificationDto.setType("SYSTEM_ALERT");
        notificationDto.setSentTime(LocalDateTime.now());

        // Mock Notifications
        notification1 = new Notification();
        notification1.setNotificationId(100L);
        notification1.setUser(adminUser);
        notification1.setMessage("System maintenance scheduled");
        notification1.setType("SYSTEM_ALERT");
        notification1.setSentTime(LocalDateTime.now());
        notification1.setStatus("SENT");
        notification1.setRead(false);

        notification2 = new Notification();
        notification2.setNotificationId(101L);
        notification2.setUser(adminUser);
        notification2.setMessage("New policy update available");
        notification2.setType("INFORMATION");
        notification2.setSentTime(LocalDateTime.now());
        notification2.setStatus("SENT");
        notification2.setRead(false);
    }

    @Test
    void testUserRegisterAdmin() {
        when(roleRepository.findByRoleName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode("securepassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(Admin.class))).thenAnswer(invocation -> {
            Admin admin = invocation.getArgument(0);
            admin.setId(1L);
            return admin;
        });

        UserDto result = userService.registerUser(adminDto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("admin@example.com", result.getEmail());
        assertEquals("PENDING", result.getStatus());
        assertEquals("ROLE_ADMIN", result.getRoleName());

        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(passwordEncoder).encode("securepassword");
        verify(userRepository).save(any(Admin.class));
    }

    @Test
    void testRegisterUser_InvalidUserType() {
        adminDto.setUserType("INVALID_TYPE");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(adminDto);
        });

        assertEquals("Invalid user type: INVALID_TYPE", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testSendNotification_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notification = invocation.getArgument(0);
            notification.setNotificationId(100L);
            return notification;
        });

        NotificationDto result = userService.sendNotification(notificationDto);

        assertNotNull(result);
        assertEquals(100L, result.getNotificationId());
        assertEquals(1L, result.getUserId());
        assertEquals("System update scheduled", result.getMessage());
        assertEquals("SYSTEM_ALERT", result.getType());
        assertEquals("SENT", result.getStatus());
        assertFalse(result.isRead());

        verify(userRepository).findById(1L);
        verify(notificationRepository).save(any(Notification.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/notifications/1"), any(NotificationDto.class));
    }

    @Test
    void testGetUserNotifications_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(notificationRepository.findByUserId(1L)).thenReturn(Arrays.asList(notification1, notification2));

        List<NotificationDto> result = userService.getUserNotifications(1L);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(100L, result.get(0).getNotificationId());
        assertEquals("System maintenance scheduled", result.get(0).getMessage());
        assertEquals("SYSTEM_ALERT", result.get(0).getType());
        assertEquals("SENT", result.get(0).getStatus());
        assertFalse(result.get(0).isRead());

        assertEquals(101L, result.get(1).getNotificationId());
        assertEquals("New policy update available", result.get(1).getMessage());
        assertEquals("INFORMATION", result.get(1).getType());
        assertEquals("SENT", result.get(1).getStatus());
        assertFalse(result.get(1).isRead());

        verify(userRepository).findById(1L);
        verify(notificationRepository).findByUserId(1L);
    }
}