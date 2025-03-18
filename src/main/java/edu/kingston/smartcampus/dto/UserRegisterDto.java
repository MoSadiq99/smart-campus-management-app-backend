package edu.kingston.smartcampus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// self register dto
@Data
public class UserRegisterDto {
    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotNull(message = "Email is required")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Address is required")
    private String address;

//    private String profileImage;

    @NotBlank(message = "User type is required")
    private String userType;
}