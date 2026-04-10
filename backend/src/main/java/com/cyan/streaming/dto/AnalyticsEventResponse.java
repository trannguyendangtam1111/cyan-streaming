package com.cyan.streaming.dto;

import java.time.LocalDateTime;

public record AnalyticsEventResponse(
        Long id,
        String eventType,
        Long movieId,
        String query,
        String actor,
        String source,
        LocalDateTime createdAt
) {
}
