package com.somosayni.identidad.infrastructure.rest;

import com.somosayni.identidad.application.command.*;
import com.somosayni.identidad.infrastructure.rest.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegistrarUsuarioCommandHandler registrarHandler;
    private final IniciarSesionCommandHandler loginHandler;
    private final CambiarPasswordCommandHandler cambiarPasswordHandler;

    public AuthController(
            RegistrarUsuarioCommandHandler registrarHandler,
            IniciarSesionCommandHandler loginHandler,
            CambiarPasswordCommandHandler cambiarPasswordHandler) {
        this.registrarHandler = registrarHandler;
        this.loginHandler = loginHandler;
        this.cambiarPasswordHandler = cambiarPasswordHandler;
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        RegistrarUsuarioCommand command = new RegistrarUsuarioCommand(request.email(), request.password(), request.rol());
        RegistrarUsuarioCommandHandler.Resultado resultado = registrarHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(resultado.usuarioId(), resultado.email(), null, resultado.token()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody IniciarSesionRequest request) {
        IniciarSesionCommand command = new IniciarSesionCommand(request.email(), request.password());
        IniciarSesionCommandHandler.Resultado resultado = loginHandler.handle(command);
        return ResponseEntity.ok(new AuthResponse(resultado.usuarioId(), resultado.email(), resultado.rol(), resultado.token()));
    }

    @PostMapping("/password")
    public ResponseEntity<MensajeResponse> cambiarPassword(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CambiarPasswordRequest request) {
        CambiarPasswordCommand command = new CambiarPasswordCommand(userId, request.passwordActual(), request.passwordNueva());
        cambiarPasswordHandler.handle(command);
        return ResponseEntity.ok(new MensajeResponse("Password cambiado exitosamente"));
    }
}
