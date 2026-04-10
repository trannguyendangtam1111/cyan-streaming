package com.cyan.streaming.controller;

import com.cyan.streaming.dto.ProgressRequest;
import com.cyan.streaming.dto.ProgressResponse;
import com.cyan.streaming.security.UserPrincipal;
import com.cyan.streaming.service.WatchProgressService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final WatchProgressService watchProgressService;

    @PostMapping
    public ProgressResponse saveProgress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProgressRequest request
    ) {
        return watchProgressService.saveProgress(userPrincipal.getUserId(), request);
    }

    @GetMapping
    public List<ProgressResponse> getProgress(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return watchProgressService.getProgress(userPrincipal.getUserId());
    }
}
