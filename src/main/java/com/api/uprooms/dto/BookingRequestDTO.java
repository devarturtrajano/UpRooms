package com.api.uprooms.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record BookingRequestDTO(
        @NotNull(message = "The user Id can not be null.")
        Long userId,

        @NotNull(message = "The room Id can not be null.")
        Long roomId,

        @NotNull(message = "The start date can not be null.")
        @FutureOrPresent(message = "The booking can not start on the past.")
        LocalDateTime startTime,

        @NotNull(message = "The ending date can not be null.")
        LocalDateTime endTime
) {}