package com.areninha.repository;

import com.areninha.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNick(String nick);
    boolean existsByEmail(String email);
    boolean existsByNick(String nick);
}