package com.somosayni.identidad.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarPasswordRequest(
        @NotBlank(message = "Password actual es obligatoria")
        String passwordActual,

        @NotBlank(message = "Password nueva es obligatoria")
        @Size(min = 8, message = "Password debe tener al menos 8 caracteres")
        String passwordNueva
) {}
