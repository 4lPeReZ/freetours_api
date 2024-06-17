package com.example.freetours.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping
    public Map<String, String> protectedEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a protected endpoint");
        return response;
    }
}
