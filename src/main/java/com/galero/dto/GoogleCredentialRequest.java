package com.galero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for receiving Google OAuth credential from frontend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCredentialRequest {

    @NotBlank(message = "Credential cannot be blank")
    private String credential;
}
