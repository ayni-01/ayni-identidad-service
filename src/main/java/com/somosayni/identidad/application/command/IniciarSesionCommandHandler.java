package com.somosayni.identidad.application.command;

import com.somosayni.identidad.application.port.UsuarioRepository;
import com.somosayni.identidad.domain.model.Usuario;
import com.somosayni.identidad.domain.service.JwtService;
import com.somosayni.identidad.domain.service.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IniciarSesionCommandHandler {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public IniciarSesionCommandHandler(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public record Resultado(
            String usuarioId,
            String email,
            String rol,
            String token
    ) {}

    public Resultado handle(IniciarSesionCommand command) {
        Usuario usuario = usuarioRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!usuario.isActivo()) {
            throw new IllegalStateException("Usuario desactivado");
        }

        if (!passwordEncoder.matches(command.password(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().name());

        return new Resultado(usuario.getId(), usuario.getEmail(), usuario.getRol().name(), token);
    }
}
