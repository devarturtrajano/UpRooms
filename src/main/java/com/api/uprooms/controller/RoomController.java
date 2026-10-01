package com.api.uprooms.controller;

import com.api.uprooms.dto.RoomDTO;
import com.api.uprooms.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@Tag(name = "Room Management", description = "Endpoints for managing rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new room", description = "Creates a new meeting room")
    @ApiResponse(responseCode = "201", description = "Room created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomDTO dto) {
        RoomDTO response = roomService.createRoom(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active rooms", description = "Returns a list of all currently active rooms")
    @ApiResponse(responseCode = "200", description = "List of active rooms retrieved successfully")
    public ResponseEntity<List<RoomDTO>> getActiveRooms() {
        List<RoomDTO> rooms = roomService.findAllActiveRooms();
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping("{id}/toggle-status")
    @Operation(summary = "Toggle room status", description = "Toggles the active status of a room")
    @ApiResponse(responseCode = "204", description = "Room status toggled successfully")
    @ApiResponse(responseCode = "404", description = "Room not found")
    public ResponseEntity<RoomDTO> toggleRoomStatus(@Parameter(description = "Room ID", required = true) @PathVariable Long id) {
        roomService.toggleRoomStatus(id);
        return ResponseEntity.noContent().build();
    }
}