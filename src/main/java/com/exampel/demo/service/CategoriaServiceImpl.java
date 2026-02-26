package com.exampel.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampel.demo.model.Categoria;
import com.exampel.demo.model.Usuario;
import com.exampel.demo.repository.CategoriaRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarPorUsuario(Long userId) {
        return categoriaRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Categoria guardar(Categoria categoria, Long userId) {
        // Aquí podrías añadir lógica extra, como validar que el nombre no sea nulo
        Usuario user = new Usuario(); // Suponiendo que tienes el objeto Usuario
        user.setId(userId);
        categoria.setUser(user);

        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public void eliminar(Long id, Long userId) {
        // Validamos que la categoría pertenezca al usuario antes de borrar
        Categoria cat = buscarPorId(id, userId);
        if (cat != null) {
            categoriaRepository.delete(cat);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id, Long userId) {
        // Buscamos por ID y verificamos pertenencia para evitar que
        // un usuario malintencionado vea categorías de otros vía ID
        return categoriaRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada o acceso denegado"));
    }

    @Override
    @Transactional
    public Categoria actualizar(Long id, Categoria categoriaDetalles, Long userId) {
        // 1. Buscamos la categoría existente usando el método que ya tenemos (que
        // valida al usuario)
        Categoria categoriaExistente = buscarPorId(id, userId);

        // 2. Actualizamos los campos
        categoriaExistente.setName(categoriaDetalles.getName());
        categoriaExistente.setIcon(categoriaDetalles.getIcon());
        categoriaExistente.setColor(categoriaDetalles.getColor());

        // 3. Guardamos los cambios
        return categoriaRepository.save(categoriaExistente);
    }
}
