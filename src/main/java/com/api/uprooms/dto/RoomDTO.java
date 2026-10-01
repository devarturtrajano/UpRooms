package com.api.uprooms.dto;

import com.api.uprooms.model.Room;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RoomDTO(
        Long id,

        @NotNull(message = "The room number can not be null.")
        Integer number,

        @NotNull(message = "The room capacity can not be null.")
        @Min(value = 1, message = "The room capacity must be greater than 1.")
        Integer capacity,

        boolean isActive
) {
    public RoomDTO(Room room) {
        this(room.getId(), room.getNumber(), room.getCapacity(), room.isActive());
    }
}