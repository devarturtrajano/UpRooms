package com.api.uprooms.dto;

import com.api.uprooms.model.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for room details")
public record RoomDTO(
        @Schema(description = "The unique ID of the room", example = "1")
        Long id,

        @Schema(description = "The room number", example = "101")
        @NotNull(message = "The room number can not be null.")
        Integer number,

        @Schema(description = "The capacity of the room", example = "10")
        @NotNull(message = "The room capacity can not be null.")
        @Min(value = 1, message = "The room capacity must be greater than 1.")
        Integer capacity,

        @Schema(description = "Whether the room is currently active", example = "true")
        boolean isActive
) {
    public RoomDTO(Room room) {
        this(room.getId(), room.getNumber(), room.getCapacity(), room.isActive());
    }
}