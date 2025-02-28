package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentDto extends UserDto {
    private String studentIdNumber;
    private String major;
    private List<Long> enrolledCourseIds; // IDs of enrolled courses
}
