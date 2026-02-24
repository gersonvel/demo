package com.exampel.demo.service;

import com.exampel.demo.model.RefreshToken;
import com.exampel.demo.repository.RefreshTokenRepository;
import com.exampel.demo.repository.UsuarioRepository;
import com.exampel.demo.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${application.security.jwt.refresh-expiration}")
    private Long refreshExpirationMs; // Añade esto a tu application.properties (ej. 604800000 para 7 días)

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional // Asegúrate de tener esta anotación
    public RefreshToken createRefreshToken(String email) {
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // NUEVO: Borrar el token anterior si existe para evitar el error de llave
        // duplicada
        refreshTokenRepository.deleteByUser(usuario);
        refreshTokenRepository.flush(); // Forzamos a que se ejecute el borrado antes del insert

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(usuario);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Por favor, inicie sesión de nuevo");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUsuarioId(Long userId) {
        var usuario = usuarioRepository.findById(userId).get();
        refreshTokenRepository.deleteByUser(usuario);
    }
}
