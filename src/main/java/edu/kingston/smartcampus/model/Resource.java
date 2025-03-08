package edu.kingston.smartcampus.model;

import edu.kingston.smartcampus.model.enums.ResourceStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    private String resourceName;
    private String type;
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private ResourceStatus availabilityStatus;

    private String location;

    @ManyToMany
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @ManyToMany(mappedBy = "resources")
    private List<Lecture> lectures;
}