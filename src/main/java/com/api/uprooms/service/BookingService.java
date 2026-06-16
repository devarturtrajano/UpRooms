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
            throw new IllegalArgumentException("A data de início deve ser anterior à data de término.");
        }

        User user = userService.findEntityById(dto.userId());
        Room room = roomService.findEntityById(dto.roomId());

        if (!room.isActive()) {
            throw new IllegalStateException("Esta sala está inativa ou em manutenção.");
        }

        boolean isOverlapping = bookingRepository.existsOverlappingBooking(
                room.getId(),
                dto.startTime(),
                dto.endTime(),
                EnumBookingStatus.BookingStatus.CONFIRMED
        );

        if (isOverlapping) {
            throw new IllegalStateException("A sala já possui uma reserva confirmada para este horário.");
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
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada."));

        booking.setStatus(EnumBookingStatus.BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    public List<BookingResponseDTO> findBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(BookingResponseDTO::new)
                .toList();
    }
}