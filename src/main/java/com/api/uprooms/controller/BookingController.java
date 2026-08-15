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
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingRequestDTO dto) {
        try {
            BookingResponseDTO response = bookingService.createBooking(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(response.id())
                    .toUri();

            return ResponseEntity.created(location).body(response);
        } catch (IllegalStateException e){
            return ResponseEntity.badRequest().body("This room is not active or in maintenance.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("This room is already booked during this time.");
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getUserBookings(@PathVariable Long userId) {
        try {
            List<BookingResponseDTO> bookings = bookingService.findBookingsByUserId(userId);
            return ResponseEntity.ok(bookings);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("User not found with ID: " + userId);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> cancelBooking(@PathVariable Long id) {
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Booking not found.");
        }
    }
}