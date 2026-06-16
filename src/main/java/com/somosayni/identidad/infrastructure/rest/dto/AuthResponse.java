package com.somosayni.identidad.infrastructure.rest.dto;

public record AuthResponse(
        String usuarioId,
        String email,
        String rol,
        String token
) {}
