package com.api.uprooms.controller;

import com.api.uprooms.dto.RoomDTO;
import com.api.uprooms.service.RoomService;
import com.api.uprooms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createRoom(@Valid @RequestBody RoomDTO dto) {
        RoomDTO response = roomService.createRoom(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<RoomDTO>> getActiveRooms() {
        List<RoomDTO> rooms = roomService.findAllActiveRooms();
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping("{id}/toggle-status")
    public ResponseEntity<Object> toggleRoomStatus(@PathVariable Long id) {
        roomService.toggleRoomStatus(id);
        return ResponseEntity.noContent().build();
    }
}