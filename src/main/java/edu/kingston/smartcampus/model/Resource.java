package edu.kingston.smartcampus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import edu.kingston.smartcampus.model.enums.ResourceEnums;

@Entity
@Table(name = "resource")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceEnums.Type type;

    @Enumerated(EnumType.STRING)
    private ResourceEnums.Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;
}