package com.cyan.streaming.service;

import com.cyan.streaming.dto.ProgressRequest;
import com.cyan.streaming.dto.ProgressResponse;
import com.cyan.streaming.exception.ResourceNotFoundException;
import com.cyan.streaming.mapper.ProgressMapper;
import com.cyan.streaming.model.Movie;
import com.cyan.streaming.model.User;
import com.cyan.streaming.model.WatchProgress;
import com.cyan.streaming.repository.UserRepository;
import com.cyan.streaming.repository.WatchProgressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WatchProgressService {

    private final WatchProgressRepository watchProgressRepository;
    private final UserRepository userRepository;
    private final MovieService movieService;
    private final ProgressMapper progressMapper;

    @Transactional
    public ProgressResponse saveProgress(Long userId, ProgressRequest request) {
        User user = getUserEntityById(userId);
        Movie movie = movieService.getMovieEntityById(request.movieId());

        WatchProgress progress = watchProgressRepository.findByUserIdAndMovieId(userId, request.movieId())
                .orElseGet(() -> WatchProgress.builder().user(user).movie(movie).build());

        progress.setTimestamp(request.timestamp());
        return progressMapper.toResponse(watchProgressRepository.save(progress));
    }

    @Transactional(readOnly = true)
    public List<ProgressResponse> getProgress(Long userId) {
        getUserEntityById(userId);
        return watchProgressRepository.findByUserIdOrderByUpdatedAtDesc(userId).stream()
                .map(progressMapper::toResponse)
                .toList();
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }
}
