package com.cyan.streaming.mapper;

import com.cyan.streaming.dto.MovieResponse;
import com.cyan.streaming.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getThumbnailUrl(),
                movie.getYoutubeEmbedUrl(),
                movie.getGenre(),
                movie.getReleaseYear(),
                roundToSingleDecimal(movie.getRating())
        );
    }

    private double roundToSingleDecimal(Double value) {
        if (value == null) {
            return 0.0;
        }
        return Math.round(value * 10.0) / 10.0;
    }
}
