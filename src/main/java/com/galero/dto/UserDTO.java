package com.galero.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.galero.model.User.UserRole;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer userId;

    @NotBlank(message = "Google ID cannot be blank")
    private String googleId;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String firstName;

    private String lastName;

    private String profilePictureUrl;

    private Integer playerId;

    private UserRole role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
