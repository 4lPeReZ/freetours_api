package com.example.freetours.controller;

import com.example.freetours.model.Role;
import com.example.freetours.model.User;
import com.example.freetours.service.RoleService;
import com.example.freetours.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = roleService.findByName(role.getName()).orElse(null);
            if (existingRole != null) {
                roles.add(existingRole);
            }
        }
        user.setRoles(roles);
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            User userToUpdate = user.get();
            userToUpdate.setUsername(userDetails.getUsername());
            userToUpdate.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            userToUpdate.setEmail(userDetails.getEmail());

            Set<Role> roles = new HashSet<>();
            for (Role role : userDetails.getRoles()) {
                Role existingRole = roleService.findByName(role.getName()).orElse(null);
                if (existingRole != null) {
                    roles.add(existingRole);
                }
            }
            userToUpdate.setRoles(roles);

            return ResponseEntity.ok(userService.saveUser(userToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
