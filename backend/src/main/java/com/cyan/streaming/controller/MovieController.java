package com.cyan.streaming.controller;

import com.cyan.streaming.dto.CommentRequest;
import com.cyan.streaming.dto.CommentResponse;
import com.cyan.streaming.dto.MovieResponse;
import com.cyan.streaming.dto.PagedResponse;
import com.cyan.streaming.dto.RatingRequest;
import com.cyan.streaming.dto.RatingResponse;
import com.cyan.streaming.security.UserPrincipal;
import com.cyan.streaming.service.MovieService;
import com.cyan.streaming.service.RateLimiterService;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final RateLimiterService rateLimiterService;

    @GetMapping
    public List<MovieResponse> getAllMovies(@RequestParam(required = false) String genre) {
        return movieService.getMovies(genre);
    }

    @GetMapping("/search")
    public List<MovieResponse> searchMovies(@RequestParam(defaultValue = "") String query) {
        return movieService.searchMovies(query);
    }

    @GetMapping("/genre/{genre}")
    public List<MovieResponse> getMoviesByGenre(@PathVariable String genre) {
        return movieService.getMovies(genre);
    }

    @GetMapping("/browse")
    public PagedResponse<MovieResponse> browseMovies(
            @RequestParam(defaultValue = "TRENDING") String section,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return movieService.browseMovies(section, genre, query, page, size);
    }

    @GetMapping("/recommend/{id}")
    public List<MovieResponse> recommendMovies(@PathVariable Long id) {
        return movieService.recommendMovies(id);
    }

    @GetMapping("/{id}")
    public MovieResponse getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping("/{id}/rating")
    public RatingResponse rateMovie(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody RatingRequest request
    ) {
        return movieService.rateMovie(id, userPrincipal.getUserId(), request);
    }

    @PostMapping("/{id}/comment")
    public CommentResponse addComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CommentRequest request
    ) {
        rateLimiterService.validateRequest(
                "comment:" + userPrincipal.getUserId(),
                8,
                Duration.ofMinutes(1)
        );
        return movieService.addComment(id, userPrincipal.getUserId(), request);
    }

    @GetMapping("/{id}/comments")
    public List<CommentResponse> getComments(@PathVariable Long id) {
        return movieService.getComments(id);
    }
}
