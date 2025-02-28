package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.scheduling.config.Task;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String groupName;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    private String description;
    private LocalDateTime creationDate;

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<File> files;
}
