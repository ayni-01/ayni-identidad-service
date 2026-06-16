package com.somosayni.identidad.application.command;

public record IniciarSesionCommand(
        String email,
        String password
) {}
