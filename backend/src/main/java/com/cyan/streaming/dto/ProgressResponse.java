package com.cyan.streaming.dto;

import java.time.LocalDateTime;

public record ProgressResponse(
        Long id,
        Long movieId,
        String title,
        String thumbnailUrl,
        String genre,
        Integer releaseYear,
        Double rating,
        Long timestamp,
        LocalDateTime updatedAt
) {
}
