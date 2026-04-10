package com.cyan.streaming.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProgressRequest(
        @NotNull(message = "must not be null")
        Long movieId,

        @NotNull(message = "must not be null")
        @Min(value = 0, message = "must be greater than or equal to 0")
        Long timestamp
) {
}
