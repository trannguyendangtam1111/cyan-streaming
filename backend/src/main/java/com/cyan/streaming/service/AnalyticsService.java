package com.cyan.streaming.service;

import com.cyan.streaming.dto.AnalyticsEventRequest;
import com.cyan.streaming.dto.AnalyticsEventResponse;
import com.cyan.streaming.mapper.AnalyticsMapper;
import com.cyan.streaming.model.AnalyticsEvent;
import com.cyan.streaming.model.AnalyticsEventType;
import com.cyan.streaming.repository.AnalyticsEventRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsMapper analyticsMapper;

    @Transactional
    public AnalyticsEventResponse trackEvent(AnalyticsEventRequest request, String actor) {
        AnalyticsEventType eventType;
        try {
            eventType = AnalyticsEventType.valueOf(request.eventType().trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown event type: " + request.eventType());
        }

        AnalyticsEvent event = analyticsEventRepository.save(AnalyticsEvent.builder()
                .eventType(eventType)
                .movieId(request.movieId())
                .query(request.query())
                .actor(actor)
                .source(request.source() == null || request.source().isBlank() ? "web" : request.source().trim())
                .build());

        return analyticsMapper.toResponse(event);
    }
}
