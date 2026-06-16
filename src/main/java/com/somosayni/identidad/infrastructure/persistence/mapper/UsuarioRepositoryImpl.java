package com.somosayni.identidad.infrastructure.persistence.mapper;

import com.somosayni.identidad.application.port.UsuarioRepository;
import com.somosayni.identidad.domain.model.Usuario;
import com.somosayni.identidad.infrastructure.persistence.entity.UsuarioEntity;
import com.somosayni.identidad.infrastructure.persistence.repository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final JpaUsuarioRepository jpaRepository;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UsuarioEntity::toDomain);
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return jpaRepository.findById(id).map(UsuarioEntity::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
