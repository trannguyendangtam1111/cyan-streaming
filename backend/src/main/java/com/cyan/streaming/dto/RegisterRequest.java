package com.cyan.streaming.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "must not be blank")
        @Size(min = 3, max = 40, message = "must be between 3 and 40 characters")
        String username,

        @NotBlank(message = "must not be blank")
        @Email(message = "must be a valid email")
        String email,

        @NotBlank(message = "must not be blank")
        @Size(min = 6, message = "must be at least 6 characters")
        String password
) {
}
