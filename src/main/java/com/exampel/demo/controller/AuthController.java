package com.exampel.demo.controller;

import com.exampel.demo.dto.ResponseDTO;
import com.exampel.demo.model.Usuario;
import com.exampel.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService; // Solo inyectamos el AuthService

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
    // try {
    // var response = authService.login(request.get("email"),
    // request.get("password"));
    // return ResponseEntity.ok(response);
    // } catch (RuntimeException e) {
    // return ResponseEntity.status(401).body(e.getMessage());
    // }
    // }

    // @PostMapping("/register")
    // public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
    // try {
    // String mensaje = authService.registrar(usuario);
    // return ResponseEntity.ok(mensaje);
    // } catch (RuntimeException e) {
    // return ResponseEntity.badRequest().body(e.getMessage());
    // }
    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            Map<String, String> tokens = authService.login(request.get("email"),
                    request.get("password"));

            // Lo metemos en el "data" (el cuarto parámetro) de tu ResponseDTO
            ResponseDTO success = new ResponseDTO(200, false, "Login exitoso", tokens);
            return ResponseEntity.ok(success);
        } catch (RuntimeException e) {
            ResponseDTO error = new ResponseDTO(401, true, e.getMessage(), null);
            return ResponseEntity.status(401).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            String mensaje = authService.registrar(usuario);
            ResponseDTO success = new ResponseDTO(200, false, mensaje, null);
            return ResponseEntity.ok(success);
        } catch (RuntimeException e) {
            ResponseDTO error = new ResponseDTO(400, true, e.getMessage(), null);
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        try {
            var response = authService.refrescarToken(request.get("refreshToken"));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}