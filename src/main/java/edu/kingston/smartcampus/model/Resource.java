package edu.kingston.smartcampus.model;

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
    private String availabilityStatus;
    private String location;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}