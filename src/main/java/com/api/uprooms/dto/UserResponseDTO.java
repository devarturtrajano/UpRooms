package com.api.uprooms.dto;

import com.api.uprooms.model.User;
import com.api.uprooms.model.enums.EnumUserRole;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        EnumUserRole.UserRole role
) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}