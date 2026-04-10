package com.cyan.streaming.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalyticsEventRequest(
        @NotBlank(message = "must not be blank")
        String eventType,
        Long movieId,
        String query,
        String source
) {
}
