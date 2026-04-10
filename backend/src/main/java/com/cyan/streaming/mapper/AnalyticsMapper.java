package com.cyan.streaming.mapper;

import com.cyan.streaming.dto.AnalyticsEventResponse;
import com.cyan.streaming.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsMapper {

    public AnalyticsEventResponse toResponse(AnalyticsEvent event) {
        return new AnalyticsEventResponse(
                event.getId(),
                event.getEventType().name(),
                event.getMovieId(),
                event.getQuery(),
                event.getActor(),
                event.getSource(),
                event.getCreatedAt()
        );
    }
}
