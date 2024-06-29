package com.example.freetours.controller;

import com.example.freetours.model.Role;
import com.example.freetours.model.User;
import com.example.freetours.service.UserService;
import com.example.freetours.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
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

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");

        return ResponseEntity.ok(response);
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

        // Incluye los roles en la respuesta
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("roles", roles);

        return response;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> updateUserProfile(@RequestBody User userDetails, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        // Only update the password if a new one is provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        User updatedUser = userService.saveUser(user);

        // Generate a new JWT token
        UserDetails userDetailsUpdated = userDetailsService.loadUserByUsername(user.getUsername());
        String newToken = jwtUtil.generateToken(userDetailsUpdated);

        Map<String, Object> response = new HashMap<>();
        response.put("token", newToken);
        response.put("user", updatedUser);
        response.put("message", "Profile updated successfully");

        return ResponseEntity.ok(response);
    }

}
