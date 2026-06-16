package com.somosayni.identidad.application.command;

import com.somosayni.identidad.application.port.UsuarioRepository;
import com.somosayni.identidad.domain.model.Usuario;
import com.somosayni.identidad.domain.service.JwtService;
import com.somosayni.identidad.domain.service.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegistrarUsuarioCommandHandler {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegistrarUsuarioCommandHandler(
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
            String token
    ) {}

    public Resultado handle(RegistrarUsuarioCommand command) {
        if (usuarioRepository.existsByEmail(command.email())) {
            throw new IllegalStateException("El email ya está registrado");
        }

        String passwordHash = passwordEncoder.encode(command.password());
        Usuario.Rol rol = Usuario.Rol.valueOf(command.rol().toUpperCase());

        Usuario usuario = new Usuario(command.email(), passwordHash, rol);
        usuario = usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().name());

        return new Resultado(usuario.getId(), usuario.getEmail(), token);
    }
}
