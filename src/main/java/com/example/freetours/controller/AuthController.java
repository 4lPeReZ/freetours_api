// src/main/java/com/example/freetours/controller/AuthController.java
package com.example.freetours.controller;

import com.example.freetours.model.User;
import com.example.freetours.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
