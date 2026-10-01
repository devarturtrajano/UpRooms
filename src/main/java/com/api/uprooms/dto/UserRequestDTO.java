package com.api.uprooms.dto;

import com.api.uprooms.model.enums.EnumUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "The user name can not be blank.")
        String name,

        @NotBlank(message = "The user email can not be blank.")
        @Email(message = "Insert a valid email.")
        String email,

        @NotBlank(message = "The password can not be blank.")
        @Size(min = 6, message = "The password must have at least 6 characters.")
        String password,

        @NotNull(message = "The user role can not be null.")
        EnumUserRole.UserRole role
) {}