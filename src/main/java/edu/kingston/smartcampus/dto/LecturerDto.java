package edu.kingston.smartcampus.dto;

import lombok.Data;

import java.util.List;

@Data
public class LecturerDto extends UserDto {
    private String department;
    private List<Long> courseIds; // IDs of courses taught
}
