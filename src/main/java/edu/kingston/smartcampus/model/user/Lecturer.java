package edu.kingston.smartcampus.model.user;

import edu.kingston.smartcampus.model.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("LECTURER")
public class Lecturer extends User {

    private String department; // e.g., "Computer Science"

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    private List<Course> coursesTaught; // Courses this lecturer teaches
}
