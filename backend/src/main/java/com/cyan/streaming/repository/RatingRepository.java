package com.cyan.streaming.repository;

import com.cyan.streaming.model.Rating;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);

    @Query("select avg(r.value) from Rating r where r.movie.id = :movieId")
    Double findAverageByMovieId(@Param("movieId") Long movieId);
}
