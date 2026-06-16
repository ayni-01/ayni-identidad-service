package com.somosayni.identidad.application.command;

import com.somosayni.identidad.application.port.UsuarioRepository;
import com.somosayni.identidad.domain.model.Usuario;
import com.somosayni.identidad.domain.service.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CambiarPasswordCommandHandler {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CambiarPasswordCommandHandler(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void handle(CambiarPasswordCommand command) {
        Usuario usuario = usuarioRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(command.passwordActual(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Password actual incorrecto");
        }

        String nuevoHash = passwordEncoder.encode(command.passwordNueva());
        usuario.cambiarPassword(nuevoHash);
        usuarioRepository.save(usuario);
    }
}
