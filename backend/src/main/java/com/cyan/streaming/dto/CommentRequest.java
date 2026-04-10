package com.cyan.streaming.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "must not be blank")
        @Size(max = 1500, message = "must be at most 1500 characters")
        String content
) {
}
