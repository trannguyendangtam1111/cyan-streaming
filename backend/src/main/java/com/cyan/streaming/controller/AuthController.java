package com.cyan.streaming.controller;

import com.cyan.streaming.dto.AuthResponse;
import com.cyan.streaming.dto.LoginRequest;
import com.cyan.streaming.dto.RegisterRequest;
import com.cyan.streaming.service.AuthService;
import com.cyan.streaming.service.RateLimiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and account access APIs")
public class AuthController {

    private final AuthService authService;
    private final RateLimiterService rateLimiterService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a user", description = "Creates a new user account and returns an authentication token.")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpServletRequest) {
        rateLimiterService.validateRequest(
                "register:" + httpServletRequest.getRemoteAddr(),
                3,
                Duration.ofHours(1)
        );
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Log in a user", description = "Authenticates a user and returns a JWT token.")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        rateLimiterService.validateRequest(
                "login:" + httpServletRequest.getRemoteAddr(),
                5,
                Duration.ofMinutes(1)
        );
        return authService.login(request);
    }
}
