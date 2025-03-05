package edu.kingston.smartcampus.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDto extends UserDto {
    private String studentIdNumber;
    private String major;
    private List<Long> enrolledCourseIds; // IDs of enrolled courses
}
