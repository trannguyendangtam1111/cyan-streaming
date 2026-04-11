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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Movies", description = "Movie management APIs")
public class MovieController {

    private final MovieService movieService;
    private final RateLimiterService rateLimiterService;

    @GetMapping
    @Operation(summary = "Get all movies", description = "Returns the movie catalog, optionally filtered by genre.")
    public List<MovieResponse> getAllMovies(@RequestParam(required = false) String genre) {
        return movieService.getMovies(genre);
    }

    @GetMapping("/search")
    @Operation(summary = "Search movies", description = "Searches movies by title or related text.")
    public List<MovieResponse> searchMovies(@RequestParam(defaultValue = "") String query) {
        return movieService.searchMovies(query);
    }

    @GetMapping("/browse")
    @Operation(summary = "Browse movies", description = "Browses movies with section, genre, text query, and pagination filters.")
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
    @Operation(summary = "Get movie recommendations", description = "Returns recommended movies related to the provided movie identifier.")
    public List<MovieResponse> recommendMovies(@PathVariable Long id) {
        return movieService.recommendMovies(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by id", description = "Returns a single movie by its identifier.")
    public MovieResponse getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping("/{id}/rating")
    @Operation(summary = "Rate a movie", description = "Creates or updates the authenticated user's rating for a movie.")
    @SecurityRequirement(name = "bearerAuth")
    public RatingResponse rateMovie(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody RatingRequest request
    ) {
        return movieService.rateMovie(id, userPrincipal.getUserId(), request);
    }

    @PostMapping("/{id}/comment")
    @Operation(summary = "Add a movie comment", description = "Adds a comment to a movie on behalf of the authenticated user.")
    @SecurityRequirement(name = "bearerAuth")
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
    @Operation(summary = "Get movie comments", description = "Returns the list of comments for a movie.")
    public List<CommentResponse> getComments(@PathVariable Long id) {
        return movieService.getComments(id);
    }
}
