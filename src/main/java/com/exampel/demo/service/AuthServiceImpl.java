package com.exampel.demo.service;

import com.exampel.demo.model.RefreshToken;
import com.exampel.demo.model.Usuario;
import com.exampel.demo.repository.UsuarioRepository;
import com.exampel.demo.security.JwtUtils;
import com.exampel.demo.service.AuthService;
import com.exampel.demo.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    // @Override
    // public Map<String, String> login(String email, String password) {
    // Usuario usuario = usuarioRepository.findByEmail(email)
    // .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // if (!passwordEncoder.matches(password, usuario.getPassword())) {
    // throw new RuntimeException("Contraseña incorrecta");
    // }

    // String accessToken = jwtUtils.generateToken(email);
    // RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);

    // return Map.of(
    // "accessToken", accessToken,
    // "refreshToken", refreshToken.getToken());
    // }

    @Override
    public Map<String, Object> login(String email, String password) { // Cambiamos a Object
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String accessToken = jwtUtils.generateToken(email);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);

        // Limpiamos el password por seguridad antes de mandarlo al mapa
        usuario.setPassword(null);

        // Usamos Map.of para crear el diccionario de respuesta
        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken(),
                "user", usuario // Agregamos el objeto completo
        );
    }

    @Override
    public String registrar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente";
    }

    @Override
    public Map<String, String> refrescarToken(String refreshTokenStr) {
        return refreshTokenService.findByToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    String email = refreshToken.getUser().getEmail();
                    String newAccessToken = jwtUtils.generateToken(email);
                    return Map.of(
                            "accessToken", newAccessToken,
                            "refreshToken", refreshToken.getToken());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token no encontrado"));
    }
}