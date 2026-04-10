package com.cyan.streaming.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "must not be blank")
        String identifier,

        @NotBlank(message = "must not be blank")
        String password
) {
}
