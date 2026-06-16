package com.api.uprooms.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record BookingRequestDTO(
        @NotNull(message = "O ID do usuário é obrigatório.")
        Long userId,

        @NotNull(message = "O ID da sala é obrigatório.")
        Long roomId,

        @NotNull(message = "A data de início é obrigatória.")
        @FutureOrPresent(message = "A reserva não pode começar no passado.")
        LocalDateTime startTime,

        @NotNull(message = "A data de fim é obrigatória.")
        LocalDateTime endTime
) {}