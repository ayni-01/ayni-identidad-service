package com.somosayni.identidad.application.command;

public record RegistrarUsuarioCommand(
        String email,
        String password,
        String rol
) {}
