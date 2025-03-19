package edu.kingston.smartcampus.controller;

import edu.kingston.smartcampus.dto.NotificationDto;
import edu.kingston.smartcampus.dto.NotificationRequest;
import edu.kingston.smartcampus.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // GET /api/users/{userId}/notifications - Fetch all notifications for a user
    @GetMapping("/users/{userId}/notifications")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_LECTURER')")
    public ResponseEntity<List<NotificationDto>> getAllNotifications(@PathVariable Long userId) {
        List<NotificationDto> notifications = notificationService.getAllNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // PUT /api/notifications/{notificationId}/read - Mark a notification as read
    @PutMapping("/notifications/{notificationId}/read")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_LECTURER')")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // POST /api/users/{userId}/notifications - Send a test notification (optional, for testing)
    @PostMapping("/users/{userId}/notifications")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LECTURER')")
    public ResponseEntity<NotificationDto> sendNotification(
            @PathVariable Long userId,
            @RequestBody NotificationRequest request) {
        NotificationDto notificationDto = notificationService.sendNotification(userId, request.getMessage(), request.getType());
        return ResponseEntity.ok(notificationDto);
    }
}

