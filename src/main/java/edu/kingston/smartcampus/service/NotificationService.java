package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.model.Notification;
import edu.kingston.smartcampus.model.user.User;
import edu.kingston.smartcampus.repository.NotificationRepository;
import edu.kingston.smartcampus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void sendNotification(Long userId, String message, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setSentTime(LocalDateTime.now());
        notification.setStatus("Sent");
        notification.setRead(false);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/notifications", notification);
    }
}