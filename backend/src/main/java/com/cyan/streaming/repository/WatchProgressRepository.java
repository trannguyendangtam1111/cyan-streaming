package com.cyan.streaming.repository;

import com.cyan.streaming.model.WatchProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchProgressRepository extends JpaRepository<WatchProgress, Long> {

    Optional<WatchProgress> findByUserIdAndMovieId(Long userId, Long movieId);

    List<WatchProgress> findByUserIdOrderByUpdatedAtDesc(Long userId);
}
