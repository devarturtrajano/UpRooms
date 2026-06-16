package com.api.uprooms.controller;

import com.api.uprooms.dto.BookingRequestDTO;
import com.api.uprooms.dto.BookingResponseDTO;
import com.api.uprooms.model.enums.EnumBookingStatus;
import com.api.uprooms.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    @Test
    @DisplayName("Should create a booking successfully and return 201 Created")
    void createBooking_WithValidData_ReturnsCreated() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        BookingRequestDTO requestDto = new BookingRequestDTO(1L, 2L, start, end);
        BookingResponseDTO responseDto = new BookingResponseDTO(
                10L, start, end, LocalDateTime.now(),
                EnumBookingStatus.BookingStatus.CONFIRMED,
                1L, "John Doe", 101
        );

        Mockito.when(bookingService.createBooking(any(BookingRequestDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
}