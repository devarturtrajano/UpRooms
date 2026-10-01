package com.api.uprooms.controller;

import com.api.uprooms.dto.UserRequestDTO;
import com.api.uprooms.dto.UserResponseDTO;
import com.api.uprooms.model.enums.EnumUserRole;
import com.api.uprooms.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Should create a user successfully and return 201 Created")
    void createUser_WithValidData_ReturnsCreated() throws Exception {
        UserRequestDTO requestDto = new UserRequestDTO("John Doe", "john@email.com", "password123", EnumUserRole.UserRole.COLLABORATOR);
        UserResponseDTO responseDto = new UserResponseDTO(1L, "John Doe", "john@email.com", EnumUserRole.UserRole.COLLABORATOR);

        Mockito.when(userService.createUser(any(UserRequestDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@email.com"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when user email is invalid")
    void createUser_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO("John Doe", "invalid-email-format", "password123", EnumUserRole.UserRole.COLLABORATOR);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(userService);
    }
}