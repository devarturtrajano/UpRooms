package com.api.uprooms.dto;

import com.api.uprooms.model.User;
import com.api.uprooms.model.enums.EnumUserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for user response")
public record UserResponseDTO(
        @Schema(description = "The unique ID of the user", example = "1")
        Long id,
        @Schema(description = "The name of the user", example = "John Doe")
        String name,
        @Schema(description = "The email of the user", example = "john@example.com")
        String email,
        @Schema(description = "The role of the user", example = "ADMIN")
        EnumUserRole.UserRole role
) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}