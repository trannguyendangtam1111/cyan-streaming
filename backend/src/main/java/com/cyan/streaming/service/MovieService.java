package com.cyan.streaming.service;

import com.cyan.streaming.dto.CommentRequest;
import com.cyan.streaming.dto.CommentResponse;
import com.cyan.streaming.dto.MovieRequest;
import com.cyan.streaming.dto.MovieResponse;
import com.cyan.streaming.dto.PagedResponse;
import com.cyan.streaming.dto.RatingRequest;
import com.cyan.streaming.dto.RatingResponse;
import com.cyan.streaming.exception.ResourceNotFoundException;
import com.cyan.streaming.mapper.CommentMapper;
import com.cyan.streaming.mapper.MovieMapper;
import com.cyan.streaming.model.Comment;
import com.cyan.streaming.model.Movie;
import com.cyan.streaming.model.Rating;
import com.cyan.streaming.model.User;
import com.cyan.streaming.repository.CommentRepository;
import com.cyan.streaming.repository.MovieRepository;
import com.cyan.streaming.repository.RatingRepository;
import com.cyan.streaming.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;
    private final MovieMapper movieMapper;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "movies", key = "#genre == null || #genre.isBlank() ? 'all' : #genre.toLowerCase()")
    public List<MovieResponse> getMovies(String genre) {
        List<Movie> movies = genre == null || genre.isBlank()
                ? movieRepository.findAllByOrderByRatingDescReleaseYearDesc()
                : movieRepository.findByGenreIgnoreCaseOrderByRatingDescReleaseYearDesc(genre);

        return movies.stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {
        return movieMapper.toResponse(getMovieEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> searchMovies(String query) {
        if (query == null || query.isBlank()) {
            return getMovies(null);
        }

        return movieRepository.findByTitleContainingIgnoreCaseOrderByRatingDescReleaseYearDesc(query).stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "movieRecommendations", key = "#id")
    public List<MovieResponse> recommendMovies(Long id) {
        Movie movie = getMovieEntityById(id);
        return movieRepository.findTop6ByGenreIgnoreCaseAndIdNotOrderByRatingDescReleaseYearDesc(movie.getGenre(), id)
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(cacheNames = {"movies", "movieRecommendations"}, allEntries = true)
    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = movieRepository.save(Movie.builder()
                .title(request.title())
                .description(request.description())
                .thumbnailUrl(request.thumbnailUrl())
                .youtubeEmbedUrl(request.youtubeEmbedUrl())
                .genre(request.genre())
                .releaseYear(request.releaseYear())
                .rating(request.rating())
                .build());

        return movieMapper.toResponse(movie);
    }

    @Transactional
    @CacheEvict(cacheNames = {"movies", "movieRecommendations"}, allEntries = true)
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = getMovieEntityById(id);
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setThumbnailUrl(request.thumbnailUrl());
        movie.setYoutubeEmbedUrl(request.youtubeEmbedUrl());
        movie.setGenre(request.genre());
        movie.setReleaseYear(request.releaseYear());
        movie.setRating(request.rating());
        return movieMapper.toResponse(movieRepository.save(movie));
    }

    @Transactional
    @CacheEvict(cacheNames = {"movies", "movieRecommendations"}, allEntries = true)
    public void deleteMovie(Long id) {
        movieRepository.delete(getMovieEntityById(id));
    }

    @Transactional
    @CacheEvict(cacheNames = {"movies", "movieRecommendations"}, allEntries = true)
    public RatingResponse rateMovie(Long movieId, Long userId, RatingRequest request) {
        Movie movie = getMovieEntityById(movieId);
        User user = getUserEntityById(userId);

        Rating rating = ratingRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseGet(() -> Rating.builder().movie(movie).user(user).build());

        rating.setValue(request.value());
        ratingRepository.save(rating);

        Double average = ratingRepository.findAverageByMovieId(movieId);
        if (average != null) {
            movie.setRating(roundToSingleDecimal(average * 2));
            movieRepository.save(movie);
        }

        return new RatingResponse(movieId, request.value(), movie.getRating());
    }

    @Transactional
    public CommentResponse addComment(Long movieId, Long userId, CommentRequest request) {
        Movie movie = getMovieEntityById(movieId);
        User user = getUserEntityById(userId);

        Comment comment = commentRepository.save(Comment.builder()
                .movie(movie)
                .user(user)
                .content(request.content())
                .build());

        return commentMapper.toResponse(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long movieId) {
        getMovieEntityById(movieId);
        return commentRepository.findByMovieIdOrderByCreatedAtDesc(movieId).stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedResponse<MovieResponse> browseMovies(String section, String genre, String query, int page, int size) {
        String normalizedSection = section == null ? "TRENDING" : section.trim().toUpperCase();
        String normalizedGenre = genre == null || genre.isBlank() ? null : genre.trim();
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();
        Pageable pageable = buildPageable(normalizedSection, page, size);
        Page<Movie> moviePage = resolveBrowsePage(normalizedSection, normalizedGenre, normalizedQuery, pageable);

        return new PagedResponse<>(
                moviePage.getContent().stream().map(movieMapper::toResponse).toList(),
                moviePage.getNumber(),
                moviePage.getSize(),
                moviePage.getTotalElements(),
                moviePage.getTotalPages(),
                moviePage.hasNext()
        );
    }

    @Transactional(readOnly = true)
    public Movie getMovieEntityById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    private Page<Movie> resolveBrowsePage(String section, String genre, String query, Pageable pageable) {
        String resolvedGenre = genre != null ? genre : resolveGenreFromSection(section);

        if (resolvedGenre != null && query != null) {
            return movieRepository.findByGenreIgnoreCaseAndTitleContainingIgnoreCase(resolvedGenre, query, pageable);
        }

        if (resolvedGenre != null) {
            return movieRepository.findByGenreIgnoreCase(resolvedGenre, pageable);
        }

        if (query != null) {
            return movieRepository.findByTitleContainingIgnoreCase(query, pageable);
        }

        return movieRepository.findAll(pageable);
    }

    private Pageable buildPageable(String section, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 24);

        Sort sort = switch (section) {
            case "NEW_RELEASES" -> Sort.by(
                    Sort.Order.desc("releaseYear"),
                    Sort.Order.desc("rating")
            );
            case "TOP_RATED" -> Sort.by(
                    Sort.Order.desc("rating"),
                    Sort.Order.desc("releaseYear")
            );
            default -> Sort.by(
                    Sort.Order.desc("rating"),
                    Sort.Order.desc("releaseYear")
            );
        };

        return PageRequest.of(safePage, safeSize, sort);
    }

    private String resolveGenreFromSection(String section) {
        return switch (section) {
            case "ACTION" -> "Action";
            case "COMEDY" -> "Comedy";
            case "DRAMA" -> "Drama";
            default -> null;
        };
    }

    private double roundToSingleDecimal(Double value) {
        if (value == null) {
            return 0.0;
        }
        return Math.round(value * 10.0) / 10.0;
    }
}
