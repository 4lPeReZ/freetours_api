// src/main/java/com/example/freetours/controller/CustomErrorController.java
package com.example.freetours.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String handleError() {
        return "Custom error page: The resource you are looking for is not available.";
    }
}
