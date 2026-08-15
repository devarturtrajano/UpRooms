package com.api.uprooms.controller;

import com.api.uprooms.dto.RoomDTO;
import com.api.uprooms.service.RoomService;
import com.api.uprooms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;

    public RoomController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Object> createRoom(@Valid @RequestBody RoomDTO dto, @PathVariable Long userId) {
        try {
            RoomDTO response = roomService.createRoom(dto, userId);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(response.id())
                    .toUri();

            return ResponseEntity.created(location).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("A room with the same number already exists.");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Only admins can perform this action.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while creating the room.");
        }

    }

    @GetMapping("/active")
    public ResponseEntity<List<RoomDTO>> getActiveRooms() {
        List<RoomDTO> rooms = roomService.findAllActiveRooms();
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping("/{userId}/{id}/toggle-status")
    public ResponseEntity<Object> toggleRoomStatus(@PathVariable Long userId, @PathVariable Long id) {
        try {
            roomService.toggleRoomStatus(userId, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User not found with ID:" + userId);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Only admins can perform this action.");
        }
    }
}