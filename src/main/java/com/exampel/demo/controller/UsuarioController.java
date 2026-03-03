package com.exampel.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampel.demo.dto.ResponseDTO;
import com.exampel.demo.model.Usuario;
import com.exampel.demo.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDTO> obtenerMiPerfil(Authentication authentication) {
        // 'authentication.getName()' extrae el email del JWT que ya validó el filtro
        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        // IMPORTANTE: Por seguridad, podrías querer ocultar el password antes de
        // enviarlo
        usuario.setPassword(null);

        return ResponseEntity.ok(new ResponseDTO(
                200,
                false,
                "Perfil recuperado exitosamente",
                usuario));
    }
}