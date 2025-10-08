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
    public Map<String, Objects> healthCheck() {
        Map<String, Objects> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Jetpack Joyride Backend is running!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/info")
    public
}
