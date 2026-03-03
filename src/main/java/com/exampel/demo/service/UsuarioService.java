package com.exampel.demo.service;

import com.exampel.demo.model.Usuario;

public interface UsuarioService {
    Usuario buscarPorEmail(String email);
}