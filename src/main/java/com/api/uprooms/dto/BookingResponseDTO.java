package com.api.uprooms.dto;

import com.api.uprooms.model.Booking;
import com.api.uprooms.model.enums.EnumBookingStatus;
import java.time.LocalDateTime;

public record BookingResponseDTO(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        EnumBookingStatus.BookingStatus status,
        Long userId,
        String userName,
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