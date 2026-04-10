package com.cyan.streaming.mapper;

import com.cyan.streaming.dto.ProgressResponse;
import com.cyan.streaming.model.WatchProgress;
import org.springframework.stereotype.Component;

@Component
public class ProgressMapper {

    public ProgressResponse toResponse(WatchProgress progress) {
        return new ProgressResponse(
                progress.getId(),
                progress.getMovie().getId(),
                progress.getMovie().getTitle(),
                progress.getMovie().getThumbnailUrl(),
                progress.getMovie().getGenre(),
                progress.getMovie().getReleaseYear(),
                progress.getMovie().getRating(),
                progress.getTimestamp(),
                progress.getUpdatedAt()
        );
    }
}
