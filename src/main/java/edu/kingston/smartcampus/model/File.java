package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group; // Nullable for personal files

    private String fileName;
    private String filePath;
    private LocalDateTime uploadDate;
}