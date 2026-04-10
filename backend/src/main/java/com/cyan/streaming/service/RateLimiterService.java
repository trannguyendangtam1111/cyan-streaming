package com.cyan.streaming.service;

import com.cyan.streaming.exception.TooManyRequestsException;
import java.time.Duration;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<String, Deque<Long>> requestBuckets = new ConcurrentHashMap<>();

    public void validateRequest(String key, int maxRequests, Duration window) {
        long now = System.currentTimeMillis();
        long cutoff = now - window.toMillis();

        Deque<Long> bucket = requestBuckets.computeIfAbsent(key, ignored -> new ConcurrentLinkedDeque<>());

        synchronized (bucket) {
            while (!bucket.isEmpty() && bucket.peekFirst() < cutoff) {
                bucket.pollFirst();
            }

            if (bucket.size() >= maxRequests) {
                throw new TooManyRequestsException("Rate limit exceeded. Please wait a moment and try again.");
            }

            bucket.addLast(now);
        }
    }
}
