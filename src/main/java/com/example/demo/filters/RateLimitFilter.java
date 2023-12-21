package com.example.demo.filters;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements WebFilter {

    private final Bucket bucket;

    public Bucket getBucket() {
        return bucket;
    }

    public RateLimitFilter() {
        Bandwidth limit = Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        } else {
            // Return a response indicating rate limit exceeded
            return exchange.getResponse().setComplete();
        }
    }

    
}
