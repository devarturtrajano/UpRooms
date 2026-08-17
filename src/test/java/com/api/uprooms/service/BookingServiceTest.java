package com.api.uprooms.service;

import com.api.uprooms.dto.BookingRequestDTO;
import com.api.uprooms.dto.BookingResponseDTO;
import com.api.uprooms.exceptions.BusinessException;
import com.api.uprooms.exceptions.ResourceNotFoundException;
import com.api.uprooms.model.Booking;
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
import java.util.List;

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
    private Room sampleRoomInactive;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "John Doe", "john@email.com", "password", EnumUserRole.UserRole.COLLABORATOR, new ArrayList<>());
        sampleRoom = new Room(2L, 101, 10, true);
        sampleRoomInactive = new Room(3L, 102, 10, false);
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
    @DisplayName("Should throw IllegalArgumentException when a booking time conflict occurs")
    void createBooking_WithTimeConflict_ThrowsIllegalArgumentException() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        LocalDateTime end = start.plusHours(2);
        BookingRequestDTO requestDto = new BookingRequestDTO(1L, 2L, start, end);

        Mockito.when(userService.findEntityById(1L)).thenReturn(sampleUser);
        Mockito.when(roomService.findEntityById(2L)).thenReturn(sampleRoom);

        Mockito.when(bookingRepository.existsOverlappingBooking(
                eq(2L), eq(start), eq(end), eq(EnumBookingStatus.BookingStatus.CONFIRMED)
        )).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookingService.createBooking(requestDto);
        });

        assertEquals("This room is already booked during this time.", exception.getMessage());
        Mockito.verify(bookingRepository, Mockito.never()).save(any());
    }
    @Test
    @DisplayName("Should throw BusinessException when a booking is made for an inactive room")
    void createBooking_withInactiveRoom_ThrowsBusinessException(){
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        LocalDateTime end = start.plusHours(2);
        BookingRequestDTO requestDto = new BookingRequestDTO(1L, 3L, start, end);

        Mockito.when(userService.findEntityById(1L)).thenReturn(sampleUser);
        Mockito.when(roomService.findEntityById(3L)).thenReturn(sampleRoomInactive);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookingService.createBooking(requestDto);
        });

        assertEquals("This room is not active or in maintenance.", exception.getMessage());

        Mockito.verify(bookingRepository, Mockito.never()).existsOverlappingBooking(any(), any(), any(), any());

        Mockito.verify(bookingRepository, Mockito.never()).save(any());
    }
    @Test
    @DisplayName("Should successfully cancel a booking")
    void cancelBooking_WithValidId_ReturnsVoid() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now().plusDays(1));
        booking.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        booking.setStatus(EnumBookingStatus.BookingStatus.CONFIRMED);
        booking.setUser(sampleUser);
        booking.setRoom(sampleRoom);

        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.of(booking));
        Mockito.when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        bookingService.cancelBooking(bookingId);

        assertEquals(EnumBookingStatus.BookingStatus.CANCELED, booking.getStatus());
        Mockito.verify(bookingRepository, Mockito.times(1)).findById(bookingId);
        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException when attempting to cancel a non-existent booking")
    void cancelBooking_WithInvalidId_ThrowsResourceNotFoundException() {
        // Arrange
        Long invalidBookingId = 99L;
        Mockito.when(bookingRepository.findById(invalidBookingId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.cancelBooking(invalidBookingId);
        });

        assertEquals("Booking not found.", exception.getMessage());
        Mockito.verify(bookingRepository, Mockito.never()).save(any());
    }
    @Test
    @DisplayName("Should successfully return a list of BookingResponseDTO for a valid user ID")
    void findBookingsByUserId_WithValidUserId_ReturnsBookingResponseDTOList() {
        Long userId = 1L;

        Booking sampleBooking = new Booking();
        sampleBooking.setStartTime(LocalDateTime.now().plusDays(1));
        sampleBooking.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        sampleBooking.setStatus(EnumBookingStatus.BookingStatus.CONFIRMED);
        sampleBooking.setUser(sampleUser);
        sampleBooking.setRoom(sampleRoom);

        Mockito.when(bookingRepository.findByUserIdFetchAll(userId))
                .thenReturn(java.util.List.of(sampleBooking));

        List<BookingResponseDTO> response = bookingService.findBookingsByUserId(userId);

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals("John Doe", response.get(0).userName());
        assertEquals(101, response.get(0).roomNumber());
        assertEquals(EnumBookingStatus.BookingStatus.CONFIRMED, response.get(0).status());

        Mockito.verify(bookingRepository, Mockito.times(1)).findByUserIdFetchAll(userId);
    }
}