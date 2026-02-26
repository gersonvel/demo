package com.exampel.demo.service;

import java.util.List;

import com.exampel.demo.model.Categoria;

public interface CategoriaService {
    List<Categoria> listarPorUsuario(Long userId);

    Categoria guardar(Categoria categoria, Long userId);

    void eliminar(Long id, Long userId);

    Categoria buscarPorId(Long id, Long userId);

    Categoria actualizar(Long id, Categoria categoria, Long userId);
}
