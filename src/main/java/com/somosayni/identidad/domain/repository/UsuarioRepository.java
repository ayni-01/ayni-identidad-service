package com.somosayni.identidad.domain.repository;

import com.somosayni.identidad.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(String id);
    Usuario save(Usuario usuario);
    boolean existsByEmail(String email);
}
