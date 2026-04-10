package com.cyan.streaming.repository;

import com.cyan.streaming.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByMovieIdOrderByCreatedAtDesc(Long movieId);
}
