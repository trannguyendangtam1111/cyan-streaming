package com.cyan.streaming.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long movieId,
        Long userId,
        String username,
        String content,
        LocalDateTime createdAt
) {
}
