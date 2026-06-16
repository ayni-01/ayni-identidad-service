package com.somosayni.identidad.domain.model;

import com.somosayni.shared.domain.model.AggregateRoot;
import java.util.regex.Pattern;

public class Usuario extends AggregateRoot {

    private String email;
    private String passwordHash;
    private Rol rol;
    private boolean activo;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0.9+_.-]+@(.+)$");

    public Usuario() {}

    public Usuario(String email, String passwordHash, Rol rol) {
        validarEmail(email);
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = true;
    }

    private void validarEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    public void cambiarPassword(String nuevaPasswordHash) {
        if (nuevaPasswordHash == null || nuevaPasswordHash.isBlank()) {
            throw new IllegalArgumentException("Password no puede estar vacío");
        }
        this.passwordHash = nuevaPasswordHash;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public enum Rol {
        TALENTO, EMPRESA, ADMIN
    }
}
