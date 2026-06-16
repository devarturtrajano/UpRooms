package com.api.uprooms.controller;

import com.api.uprooms.dto.BookingRequestDTO;
import com.api.uprooms.dto.BookingResponseDTO;
import com.api.uprooms.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO dto) {
        BookingResponseDTO response = bookingService.createBooking(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable Long userId) {
        List<BookingResponseDTO> bookings = bookingService.findBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}