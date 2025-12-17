package com.app.tradeguess.controller.api;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/simple")
    public Map<String, String> simpleGet() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "GET request successful");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    @PostMapping("/simple")
    public Map<String, Object> simplePost(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "POST request successful");
        response.put("received", request);
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
}