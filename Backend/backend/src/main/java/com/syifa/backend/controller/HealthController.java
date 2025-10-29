package com.syifa.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class HealthController {
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Syifa Backend is running!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "CS6_Syifa_Backend");
        response.put("version", "1.0");
        response.put("decsription", "Backend API for the Syifa Backend.");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("players", "api/players");
        endpoints.put("scores", "api/scores");
        endpoints.put("leaderboard", "api/scores/leaderboard");
        endpoints.put("health", "api/health");

        response.put("endpoints", endpoints);

        return response;
    }
}
