package com.example.demo.controllers;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dimension;
import com.example.demo.Perimeter;
import com.example.demo.filters.RateLimitFilter;

@RestController
public class Controller {

    @Autowired
    private RateLimitFilter rateLimitFilter;

    @PostMapping(value = "/api/v1/perimeter/rectangle")
    public ResponseEntity<Perimeter> rectangle(@RequestBody Dimension dimensions) {
    
            if (rateLimitFilter.getBucket().tryConsume(1)) {
                return ResponseEntity.ok(new Perimeter("rectangle",
                        (double) 2 * (dimensions.getLength() + dimensions.getBreadth())));
            }
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}