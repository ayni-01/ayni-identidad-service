package com.somosayni.identidad.infrastructure.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarUsuarioRequest(
        @Email(message = "Email inválido")
        @NotBlank(message = "Email es obligatorio")
        String email,

        @NotBlank(message = "Password es obligatorio")
        @Size(min = 8, message = "Password debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "Rol es obligatorio")
        String rol
) {}
