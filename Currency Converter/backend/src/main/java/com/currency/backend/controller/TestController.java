package com.currency.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "All Good !!! Backend is running 🚀";
    }

    // ✅ Public Health Endpoint
    @GetMapping("/health")
    public String health() {
        return "Backend is running!";
    }
}
