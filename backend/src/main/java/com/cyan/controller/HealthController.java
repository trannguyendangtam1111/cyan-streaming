package com.cyan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Operational health check APIs")
public class HealthController {

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Health check",
            description = "Lightweight endpoint used for uptime monitoring and cloud health checks."
    )
    public HealthResponse health() {
        return new HealthResponse("UP");
    }

    @Schema(name = "HealthResponse", description = "Lightweight service health response")
    public record HealthResponse(
            @Schema(description = "Current service status", example = "UP")
            String status
    ) {
    }
}
