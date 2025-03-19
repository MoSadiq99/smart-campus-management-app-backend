package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.NotificationDto;
import edu.kingston.smartcampus.model.Notification;
import edu.kingston.smartcampus.model.user.User;
import edu.kingston.smartcampus.repository.NotificationRepository;
import edu.kingston.smartcampus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public NotificationDto sendNotification(Long userId, String message, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setSentTime(LocalDateTime.now());
        notification.setRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(savedNotification);

        // Send to WebSocket topic matching frontend subscription
        messagingTemplate.convertAndSend("/topic/users/" + userId + "/notifications", notificationDto);

        return notificationDto;
    }

    public List<NotificationDto> getAllNotifications(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(NotificationDto::new)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}