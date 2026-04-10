package com.cyan.streaming.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieRequest(
        @NotBlank(message = "must not be blank")
        String title,

        @NotBlank(message = "must not be blank")
        @Size(max = 2000, message = "must be at most 2000 characters")
        String description,

        @NotBlank(message = "must not be blank")
        String thumbnailUrl,

        @NotBlank(message = "must not be blank")
        String youtubeEmbedUrl,

        @NotBlank(message = "must not be blank")
        String genre,

        @NotNull(message = "must not be null")
        @Min(value = 1888, message = "must be a valid release year")
        @Max(value = 2100, message = "must be a valid release year")
        Integer releaseYear,

        @NotNull(message = "must not be null")
        @Min(value = 0, message = "must be at least 0")
        @Max(value = 10, message = "must be at most 10")
        Double rating
) {
}
