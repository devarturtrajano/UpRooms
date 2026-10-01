package com.api.uprooms.dto;

import com.api.uprooms.model.Booking;
import com.api.uprooms.model.enums.EnumBookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO for booking response")
public record BookingResponseDTO(
        @Schema(description = "The unique ID of the booking", example = "1")
        Long id,
        @Schema(description = "The start time of the booking", example = "2026-10-02T10:00:00")
        LocalDateTime startTime,
        @Schema(description = "The end time of the booking", example = "2026-10-02T12:00:00")
        LocalDateTime endTime,
        @Schema(description = "The time the booking was created", example = "2026-10-01T15:00:00")
        LocalDateTime createdAt,
        @Schema(description = "The status of the booking", example = "CONFIRMED")
        EnumBookingStatus.BookingStatus status,
        @Schema(description = "The ID of the user who made the booking", example = "1")
        Long userId,
        @Schema(description = "The name of the user who made the booking", example = "John Doe")
        String userName,
        @Schema(description = "The room number", example = "101")
        Integer roomNumber
) {
    public BookingResponseDTO(Booking booking) {
        this(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getCreatedAt(),
                booking.getStatus(),
                booking.getUser().getId(),
                booking.getUser().getName(),
                booking.getRoom().getNumber()
        );
    }
}