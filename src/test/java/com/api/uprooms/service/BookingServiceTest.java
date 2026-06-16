package com.api.uprooms.service;

import com.api.uprooms.dto.BookingRequestDTO;
import com.api.uprooms.dto.BookingResponseDTO;
import com.api.uprooms.model.Room;
import com.api.uprooms.model.User;
import com.api.uprooms.model.enums.EnumBookingStatus;
import com.api.uprooms.model.enums.EnumUserRole;
import com.api.uprooms.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private BookingService bookingService;

    private User sampleUser;
    private Room sampleRoom;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "John Doe", "john@email.com", "password", EnumUserRole.UserRole.COLLABORATOR, new ArrayList<>());
        sampleRoom = new Room(2L, 101, 10, true);
    }

    @Test
    @DisplayName("Should successfully create a booking when there are no time conflicts")
    void createBooking_WithNoConflicts_ReturnsBookingResponseDTO() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        LocalDateTime end = start.plusHours(2);
        BookingRequestDTO requestDto = new BookingRequestDTO(1L, 2L, start, end);

        Mockito.when(userService.findEntityById(1L)).thenReturn(sampleUser);
        Mockito.when(roomService.findEntityById(2L)).thenReturn(sampleRoom);

        Mockito.when(bookingRepository.existsOverlappingBooking(
                eq(2L), eq(start), eq(end), eq(EnumBookingStatus.BookingStatus.CONFIRMED)
        )).thenReturn(false);

        Mockito.when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BookingResponseDTO response = bookingService.createBooking(requestDto);

        assertNotNull(response);
        assertEquals(EnumBookingStatus.BookingStatus.CONFIRMED, response.status());
        assertEquals(101, response.roomNumber());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when a booking time conflict occurs")
    void createBooking_WithTimeConflict_ThrowsIllegalStateException() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        LocalDateTime end = start.plusHours(2);
        BookingRequestDTO requestDto = new BookingRequestDTO(1L, 2L, start, end);

        Mockito.when(userService.findEntityById(1L)).thenReturn(sampleUser);
        Mockito.when(roomService.findEntityById(2L)).thenReturn(sampleRoom);

        Mockito.when(bookingRepository.existsOverlappingBooking(
                eq(2L), eq(start), eq(end), eq(EnumBookingStatus.BookingStatus.CONFIRMED)
        )).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bookingService.createBooking(requestDto);
        });

        assertEquals("A sala já possui uma reserva confirmada para este horário.", exception.getMessage());
        Mockito.verify(bookingRepository, Mockito.never()).save(any());
    }
}