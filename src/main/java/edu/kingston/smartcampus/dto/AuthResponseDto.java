package edu.kingston.smartcampus.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private UserDto user;

    public AuthResponseDto(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }
}