package com.somosayni.identidad.application.command;

public record CambiarPasswordCommand(
        String userId,
        String passwordActual,
        String passwordNueva
) {}
