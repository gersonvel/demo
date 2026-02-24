package com.exampel.demo.service;

import com.exampel.demo.model.Usuario;
import java.util.Map;

public interface AuthService {
    Map<String, String> login(String email, String password);

    String registrar(Usuario usuario);

    Map<String, String> refrescarToken(String refreshToken);
}