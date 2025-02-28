package edu.kingston.smartcampus.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String profileImage;
    private String status;
    private String roleName;
    private String userType;
}