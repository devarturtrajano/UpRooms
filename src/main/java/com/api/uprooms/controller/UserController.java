package com.api.uprooms.controller;

import com.api.uprooms.dto.UserRequestDTO;
import com.api.uprooms.dto.UserResponseDTO;
import com.api.uprooms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO response = userService.createUser(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getAllUsers(@PathVariable Long userId) {
        try {
            List<UserResponseDTO> users = userService.findAll(userId);
            return ResponseEntity.ok(users);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Only admins can perform this action.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User not found with ID: " + userId);
        }
    }
}