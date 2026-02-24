package com.exampel.demo.repository;

import com.exampel.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Esto es magia de Spring: crea la consulta automáticamente por el nombre del
    // método
    Optional<Usuario> findByEmail(String email);
}