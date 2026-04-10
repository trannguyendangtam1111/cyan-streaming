package com.cyan.streaming.controller;

import com.cyan.streaming.dto.ProgressRequest;
import com.cyan.streaming.dto.ProgressResponse;
import com.cyan.streaming.security.UserPrincipal;
import com.cyan.streaming.service.WatchProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Watch Progress", description = "Authenticated APIs for saving and retrieving watch progress")
@SecurityRequirement(name = "bearerAuth")
public class ProgressController {

    private final WatchProgressService watchProgressService;

    @PostMapping
    @Operation(summary = "Save watch progress", description = "Persists the authenticated user's watch progress for a movie.")
    public ProgressResponse saveProgress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProgressRequest request
    ) {
        return watchProgressService.saveProgress(userPrincipal.getUserId(), request);
    }

    @GetMapping
    @Operation(summary = "Get watch progress", description = "Returns all saved watch progress records for the authenticated user.")
    public List<ProgressResponse> getProgress(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return watchProgressService.getProgress(userPrincipal.getUserId());
    }
}
