package com.api.uprooms.dto;

import com.api.uprooms.model.enums.EnumUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Insira um e-mail válido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password,

        @NotNull(message = "O perfil (role) é obrigatório.")
        EnumUserRole.UserRole role
) {}