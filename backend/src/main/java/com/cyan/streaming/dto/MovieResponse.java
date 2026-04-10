package com.cyan.streaming.dto;

public record MovieResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        String youtubeEmbedUrl,
        String genre,
        Integer releaseYear,
        Double rating
) {
}
