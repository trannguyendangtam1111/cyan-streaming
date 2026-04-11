package com.cyan.streaming.repository;

import com.cyan.streaming.model.Movie;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByOrderByRatingDescReleaseYearDesc(Pageable pageable);

    List<Movie> findByGenreIgnoreCaseOrderByRatingDescReleaseYearDesc(String genre, Pageable pageable);

    List<Movie> findByTitleContainingIgnoreCaseOrderByRatingDescReleaseYearDesc(String title, Pageable pageable);

    List<Movie> findTop6ByGenreIgnoreCaseAndIdNotOrderByRatingDescReleaseYearDesc(String genre, Long id);

    Page<Movie> findByGenreIgnoreCase(String genre, Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String query, Pageable pageable);

    Page<Movie> findByGenreIgnoreCaseAndTitleContainingIgnoreCase(String genre, String query, Pageable pageable);
}
