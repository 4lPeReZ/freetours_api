package com.example.freetours.controller;

import com.example.freetours.model.Role;
import com.example.freetours.model.User;
import com.example.freetours.service.UserService;
import com.example.freetours.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Initialize roles set if null
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        // Assign roles
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role);
        }
        user.setRoles(roles);

        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.saveUser(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody User user) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            throw new Exception("Invalid username or password", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return Map.of("token", token);
    }
}
