package com.api.uprooms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "DTO for booking request")
public record BookingRequestDTO(
        @Schema(description = "The ID of the user creating the booking", example = "1")
        @NotNull(message = "The user Id can not be null.")
        Long userId,

        @Schema(description = "The ID of the room being booked", example = "1")
        @NotNull(message = "The room Id can not be null.")
        Long roomId,

        @Schema(description = "The start time of the booking", example = "2026-10-02T10:00:00")
        @NotNull(message = "The start date can not be null.")
        @FutureOrPresent(message = "The booking can not start on the past.")
        LocalDateTime startTime,

        @Schema(description = "The end time of the booking", example = "2026-10-02T12:00:00")
        @NotNull(message = "The ending date can not be null.")
        LocalDateTime endTime
) {}