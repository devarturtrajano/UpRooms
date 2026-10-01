package com.api.uprooms.dto;

import com.api.uprooms.model.enums.EnumUserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for user creation request")
public record UserRequestDTO(
        @Schema(description = "The name of the user", example = "John Doe")
        @NotBlank(message = "The user name can not be blank.")
        String name,

        @Schema(description = "The email of the user", example = "john@example.com")
        @NotBlank(message = "The user email can not be blank.")
        @Email(message = "Insert a valid email.")
        String email,

        @Schema(description = "The password of the user", example = "password123")
        @NotBlank(message = "The password can not be blank.")
        @Size(min = 6, message = "The password must have at least 6 characters.")
        String password,

        @Schema(description = "The role of the user", example = "ADMIN")
        @NotNull(message = "The user role can not be null.")
        EnumUserRole.UserRole role
) {}