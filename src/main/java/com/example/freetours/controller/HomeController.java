// src/main/java/com/example/freetours/controller/HomeController.java
package com.example.freetours.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to FreeTours API!";
    }
}
