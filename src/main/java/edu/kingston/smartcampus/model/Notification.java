package edu.kingston.smartcampus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import edu.kingston.smartcampus.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Back reference to User
    private User user;

    @Column(nullable = false)
    private String message;

    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentTime;

    @Column(name = "`read`", nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 0")
    private boolean read = false;

    private String status;
}