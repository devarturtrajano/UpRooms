package com.api.uprooms.dto;

import com.api.uprooms.model.Room;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RoomDTO(
        Long id,

        @NotNull(message = "O número da sala é obrigatório.")
        Integer number,

        @NotNull(message = "A capacidade é obrigatória.")
        @Min(value = 1, message = "A capacidade deve ser de pelo menos 1 pessoa.")
        Integer capacity,

        boolean isActive
) {
    public RoomDTO(Room room) {
        this(room.getId(), room.getNumber(), room.getCapacity(), room.isActive());
    }
}