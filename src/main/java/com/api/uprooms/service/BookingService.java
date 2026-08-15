package com.api.uprooms.service;

import com.api.uprooms.dto.BookingRequestDTO;
import com.api.uprooms.dto.BookingResponseDTO;
import com.api.uprooms.model.Booking;
import com.api.uprooms.model.Room;
import com.api.uprooms.model.User;
import com.api.uprooms.model.enums.EnumBookingStatus;
import com.api.uprooms.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;

    public BookingService(BookingRepository bookingRepository, UserService userService, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.roomService = roomService;
    }

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        if (dto.startTime().isAfter(dto.endTime()) || dto.startTime().isEqual(dto.endTime())) {
            throw new IllegalArgumentException("The start time must be before the end time.");
        }

        User user = userService.findEntityById(dto.userId());
        Room room = roomService.findEntityById(dto.roomId());

        if (!room.isActive()) {
            throw new IllegalStateException("This room is not active or in maintenance.");
        }

        boolean isOverlapping = bookingRepository.existsOverlappingBooking(
                room.getId(),
                dto.startTime(),
                dto.endTime(),
                EnumBookingStatus.BookingStatus.CONFIRMED
        );

        if (isOverlapping) {
            throw new IllegalArgumentException("This room is already booked during this time.");
        }

        Booking booking = new Booking();
        booking.setStartTime(dto.startTime());
        booking.setEndTime(dto.endTime());
        booking.setStatus(EnumBookingStatus.BookingStatus.CONFIRMED);
        booking.setUser(user);
        booking.setRoom(room);

        booking = bookingRepository.save(booking);
        return new BookingResponseDTO(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        booking.setStatus(EnumBookingStatus.BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    public List<BookingResponseDTO> findBookingsByUserId(Long userId) {
        try {
            return bookingRepository.findByUserIdFetchAll(userId)
                    .stream()
                    .map(BookingResponseDTO::new)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
}