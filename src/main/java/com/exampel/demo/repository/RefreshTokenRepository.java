package com.exampel.demo.repository;

import com.exampel.demo.model.RefreshToken;
import com.exampel.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(Usuario user);
}
