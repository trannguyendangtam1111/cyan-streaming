package com.cyan.streaming.controller;

import com.cyan.streaming.dto.MessageResponse;
import com.cyan.streaming.dto.MovieRequest;
import com.cyan.streaming.dto.MovieResponse;
import com.cyan.streaming.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/movies")
@RequiredArgsConstructor
@Tag(name = "Admin Movies", description = "Administrative APIs for managing the movie catalog")
@SecurityRequirement(name = "bearerAuth")
public class AdminMovieController {

    private final MovieService movieService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a movie", description = "Creates a new movie entry in the catalog.")
    public MovieResponse createMovie(@Valid @RequestBody MovieRequest request) {
        return movieService.createMovie(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a movie", description = "Updates an existing movie by identifier.")
    public MovieResponse updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        return movieService.updateMovie(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a movie", description = "Removes a movie from the catalog.")
    public MessageResponse deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new MessageResponse("Movie deleted successfully");
    }
}
