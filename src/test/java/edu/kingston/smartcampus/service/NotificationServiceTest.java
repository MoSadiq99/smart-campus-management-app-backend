package edu.kingston.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.kingston.smartcampus.model.Notification;
import edu.kingston.smartcampus.model.user.Student;
import edu.kingston.smartcampus.repository.NotificationRepository;
import edu.kingston.smartcampus.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationService notificationService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
    }

    @Test
    void testSendNotification() {
        Long userId = 1L;
        String message = "Test Notification";
        String type = "MESSAGE";

        when(userRepository.findById(userId)).thenReturn(Optional.of(student));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.sendNotification(userId, message, type);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(notificationCaptor.capture());
        Notification savedNotification = notificationCaptor.getValue();

        assertNotNull(savedNotification);
        assertEquals(student, savedNotification.getUser());
        assertEquals(message, savedNotification.getMessage());
        assertEquals(type, savedNotification.getType());
        assertEquals("Sent", savedNotification.getStatus());
        assertFalse(savedNotification.isRead());

        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq(student.getUsername()), eq("/topic/notifications"), any(Notification.class));
    }
}
