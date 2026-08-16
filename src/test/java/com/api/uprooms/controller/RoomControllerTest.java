package com.api.uprooms.controller;

import com.api.uprooms.dto.RoomDTO;
import com.api.uprooms.model.User;
import com.api.uprooms.model.enums.EnumUserRole;
import com.api.uprooms.service.RoomService;
import com.api.uprooms.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should create a room successfully and return 201 Created")
    void createRoom_WithValidData_ReturnsCreated() throws Exception {
        RoomDTO requestDto = new RoomDTO(null, 101, 10, true);
        RoomDTO responseDto = new RoomDTO(1L, 101, 10, true);

        Mockito.when(roomService.createRoom(any(RoomDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value(101))
                .andExpect(jsonPath("$.capacity").value(10));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when room capacity is less than 1")
    void createRoom_WithInvalidCapacity_ReturnsBadRequest() throws Exception {
        RoomDTO invalidRequest = new RoomDTO(1L, 102, 0, true);

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should return 200 OK when listing active rooms")
    void getActiveRooms_ReturnsListAnd200Ok() throws Exception {
        List<RoomDTO> activeRooms = List.of(
                new RoomDTO(1L, 101, 10, true),
                new RoomDTO(2L, 102, 15, true)
        );

        Mockito.when(roomService.findAllActiveRooms()).thenReturn(activeRooms);

        mockMvc.perform(get("/api/v1/rooms/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].number").value(101));
    }

    @Test
    @DisplayName("Should toggle room status and return 204 No Content")
    void toggleRoomStatus_ReturnsNoContent() throws Exception {
        Long roomId = 1L;
        Mockito.doNothing().when(roomService).toggleRoomStatus(roomId);

        mockMvc.perform(patch("/api/v1/rooms/{id}/toggle-status", roomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(roomService, Mockito.times(1)).toggleRoomStatus(roomId);
    }
}