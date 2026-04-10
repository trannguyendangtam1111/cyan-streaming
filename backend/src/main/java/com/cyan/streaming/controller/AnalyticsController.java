package com.cyan.streaming.controller;

import com.cyan.streaming.dto.AnalyticsEventRequest;
import com.cyan.streaming.dto.AnalyticsEventResponse;
import com.cyan.streaming.security.UserPrincipal;
import com.cyan.streaming.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Analytics tracking APIs")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/event")
    @Operation(summary = "Track an analytics event", description = "Captures a client analytics event for reporting and monitoring.")
    public AnalyticsEventResponse trackEvent(
            @Valid @RequestBody AnalyticsEventRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String actor = userPrincipal != null ? userPrincipal.getUsername() : "anonymous";
        return analyticsService.trackEvent(request, actor);
    }
}
