package com.cyan.streaming.dto;

public record RatingResponse(
        Long movieId,
        Integer userRating,
        Double averageRating
) {
}
